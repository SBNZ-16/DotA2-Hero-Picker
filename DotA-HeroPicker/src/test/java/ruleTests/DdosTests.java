package ruleTests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.drools.core.time.SessionPseudoClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.heropicker.facts.ddos.IpAccessFact;


// set to trigger a DDOS attack if there are 3 requests from the same Ip address within one second
public class DdosTests {
	
	
	
	private ByteArrayOutputStream redirectOutputStream() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		System.setOut(ps);
		return baos;
	}
	
	private KieSession getKieSession() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieSession kSession = kContainer.newKieSession("ddos-test-ksession");
		return kSession;
	}
	
	// all requests come from same Ip address, but there is enough time between them
	@Test
	public void noDdosAttackEnoughTimeDifferenceTest() {
		
		ByteArrayOutputStream ps = redirectOutputStream();
		KieSession kSession = getKieSession();
		
		SessionPseudoClock clock = kSession.getSessionClock();
		
		for (int i = 0; i < 5; i++) {
			IpAccessFact fact = new IpAccessFact("1");
			kSession.insert(fact);
			kSession.fireAllRules();
			clock.advanceTime(500, TimeUnit.MILLISECONDS);
		}
		System.out.flush();
		
		String prints = ps.toString();
		Assertions.assertEquals(0, StringUtils.countMatches(prints, "DDOS spotted"));
	}
	
	// there is enough requests to trigger an ddos attack, but they come from seperate Ip address, so it does not happen
	@Test
	public void twoSeperateIpsNoDdos() {
		ByteArrayOutputStream ps = redirectOutputStream();
		KieSession kSession = getKieSession();
		
		SessionPseudoClock clock = kSession.getSessionClock();
		
		for (int i = 0; i < 10; i++) {
			IpAccessFact fact = new IpAccessFact(Integer.toString(i%2));
			kSession.insert(fact);
			kSession.fireAllRules();
			clock.advanceTime(300, TimeUnit.MILLISECONDS);
		}
		System.out.flush();
		
		String prints = ps.toString();
		Assertions.assertEquals(0, StringUtils.countMatches(prints, "DDOS spotted"));
	}
	
	// there is enough requests from the same Ip address to trigger a ddos attack
	@Test
	public void ddosAttackTest() {
		ByteArrayOutputStream ps = redirectOutputStream();
		KieSession kSession = getKieSession();
		
		SessionPseudoClock clock = kSession.getSessionClock();
		
		for (int i = 0; i < 5; i++) {
			IpAccessFact fact = new IpAccessFact("1");
			kSession.insert(fact);
			kSession.fireAllRules();
			clock.advanceTime(300, TimeUnit.MILLISECONDS);
		}
		System.out.flush();
		
		String prints = ps.toString();
		Assertions.assertEquals(3, StringUtils.countMatches(prints, "DDOS spotted"));
	}

}
