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


// set to trigger a DDOS attack if there are 3 requests from the same IP address within one second
// or 6 requests from requests from the different IP address within one second
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
	
	// all requests come from same IP address, but there is enough time between them
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
		Assertions.assertEquals(0, StringUtils.countMatches(prints, "DDOS from different IPs"));
		Assertions.assertEquals(0, StringUtils.countMatches(prints, "DDOS from same IP spotted"));
	}
	
	// there is enough requests to trigger an ddos attack from same IP, but they come from seperate IP address, so it does not happen
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
		Assertions.assertEquals(0, StringUtils.countMatches(prints, "DDOS from different IPs"));
		Assertions.assertEquals(0, StringUtils.countMatches(prints, "DDOS from same IP spotted"));
	}
	
	// there is enough requests from the same IP address to trigger a ddos attack
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
		Assertions.assertEquals(0, StringUtils.countMatches(prints, "DDOS from different IPs"));
		Assertions.assertEquals(3, StringUtils.countMatches(prints, "DDOS from same IP spotted"));
	}
	
	// there is enough requests from different IPs to trigger different IPs DDOS attack, 
	// and in this case this attacks has priority over same IP DDOS attack
	@Test
	public void threeSameIpRestDifferent() {
		ByteArrayOutputStream ps = redirectOutputStream();
		KieSession kSession = getKieSession();
		
		SessionPseudoClock clock = kSession.getSessionClock();
		String[] ips = new String[]{"1","1","2","3","4","5","1"};
		
		for (int i = 0; i < 7; i++) {
			IpAccessFact fact = new IpAccessFact(ips[i]);
			kSession.insert(fact);
			kSession.fireAllRules();
			clock.advanceTime(100, TimeUnit.MILLISECONDS);
		}
		System.out.flush();
		
		String prints = ps.toString();
		Assertions.assertEquals(2, StringUtils.countMatches(prints, "DDOS from different IPs"));
		Assertions.assertEquals(0, StringUtils.countMatches(prints, "DDOS from same IP spotted"));
	}

	// Same IP DDOS attack activates after third request, then different IPs DDOS attack activates four times
	@Test
	public void fourSameIpRestDifferent() {
		ByteArrayOutputStream ps = redirectOutputStream();
		KieSession kSession = getKieSession();
		
		SessionPseudoClock clock = kSession.getSessionClock();
		String[] ips = new String[]{"1","1","1","2","3","4","5","6","1"};
		
		for (int i = 0; i < 9; i++) {
			IpAccessFact fact = new IpAccessFact(ips[i]);
			kSession.insert(fact);
			kSession.fireAllRules();
			clock.advanceTime(50, TimeUnit.MILLISECONDS);
		}
		System.out.flush();
		
		String prints = ps.toString();
		Assertions.assertEquals(4, StringUtils.countMatches(prints, "DDOS from different IPs"));
		Assertions.assertEquals(1, StringUtils.countMatches(prints, "DDOS from same IP spotted"));

	}
}
