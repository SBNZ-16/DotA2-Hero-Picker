import requests

import json
from fake_useragent import UserAgent 
from bs4 import BeautifulSoup

from PIL import Image
from io import BytesIO

import time


def create_header():
    ua = UserAgent()
    return {'User-Agent': str(ua.chrome)}

def scrape_hero_info(url_fragment, hero_name, header):
    hero = {}
    hero['name'] = hero_name

    scrape_general_info(url_fragment, header, hero)
    scrape_winrates(url_fragment, header, hero)

    save_json(hero, url_fragment)


def scrape_general_info(url_fragment, header, hero, scrape_img=False):
    url = 'https://www.dotabuff.com/heroes/%s' %(url_fragment,)
    htmlContent = requests.get(url, headers=header).content
    soup = BeautifulSoup(htmlContent)

    roles_dirty = soup.find('h1').find('small').text
    roles_clean =  [role.strip() for role in roles_dirty.split(',')]
    hero['attack_type'] = roles_clean[0]
    hero['roles'] = roles_clean[1: len(roles_clean)]

    hero['overall_winrate'] = soup.find_all('dd')[1].find('span').text

    hero['lanes'] = []
    lanes_tbody = soup.find_all('tbody')[1]
    for row in lanes_tbody:
        cells = row.find_all('td')
        hero['lanes'].append({'lane': cells[0].text, 'presence': cells[1].text, 'win_rate': cells[2].text})

    attribute_table = soup.find('table', {'class': 'main'})
    attribute_tbody = attribute_table.contents[0]
    primary_attr_dirty = attribute_tbody.attrs['class'][0]
    hero['primary_attribute'] = primary_attr_dirty.split('-')[1]

    if scrape_img:
        image_src = soup.find('img', {'class': 'image-avatar image-hero'}).attrs['src']
        save_img(image_src, url_fragment)




def scrape_winrates(url_fragment, header, hero):
    url = 'https://www.dotabuff.com/heroes/%s/counters' %(url_fragment,)
    htmlContent = requests.get(url, headers=header).content
    soup = BeautifulSoup(htmlContent)

    winrate_table = soup.find_all('table', {'class': 'sortable'})[0]
    winrate_tbody = winrate_table.find_all('tbody')[0]

    win_rates = {}
    disadvantages = {}
    for row in winrate_tbody:
        cells = row.find_all('td')
        hero_name = cells[1].text
        winrate = cells[3].text
        disadvantage = cells[2].text
        win_rates[hero_name] = winrate
        disadvantages[hero_name] = disadvantage

    hero['win_rates'] = win_rates
    hero['disadvantages'] = disadvantages

    
def save_img(image_src, url_fragment):
    image_response = requests.get('https://www.dotabuff.com%s' %(image_src))
    bytes = BytesIO(image_response.content)
    image = Image.open(bytes)
    image.save('./icons/%s.jpg' %(url_fragment))

def save_json(result, url_fragment):
    with open('./scraped_data/%s.json' %(url_fragment), 'w', encoding='utf-8') as f:
        json.dump(result, f, ensure_ascii=False, indent=4)


def get_mapping_list():
    ret_val = []
    ret_val.append(('abaddon', 'Abaddon'))
    ret_val.append(('alchemist', 'Alchemist'))
    ret_val.append(('ancient-apparition', 'Ancient Apparition'))
    ret_val.append(('anti-mage', 'Anti-Mage'))
    ret_val.append(('arc-warden', 'Arc Warden'))
    ret_val.append(('axe', 'Axe'))
    ret_val.append(('bane', 'Bane'))
    ret_val.append(('batrider', 'Batrider'))
    ret_val.append(('beastmaster', 'Beastmaster'))
    ret_val.append(('bloodseeker', 'Bloodseeker'))
    ret_val.append(('bounty-hunter', 'Bounty Hunter'))
    ret_val.append(('brewmaster', 'Brewmaster'))
    ret_val.append(('bristleback', 'Bristleback'))
    ret_val.append(('broodmother', 'Broodmother'))
    ret_val.append(('centaur-warrunner', 'Centaur Warrunner'))
    ret_val.append(('chaos-knight', 'Chaos Knight'))
    ret_val.append(('chen', 'Chen'))
    ret_val.append(('clinkz', 'Clinkz'))
    ret_val.append(('clockwerk', 'Clockwerk'))
    ret_val.append(('crystal-maiden', 'Crystal Maiden'))
    ret_val.append(('dark-seer', 'Dark Seer'))
    ret_val.append(('dark-willow', 'Dark Willow'))
    ret_val.append(('dazzle', 'Dazzle'))
    ret_val.append(('death-prophet', 'Death Prophet'))
    ret_val.append(('disruptor', 'Disruptor'))
    ret_val.append(('doom', 'Doom'))
    ret_val.append(('dragon-knight', 'Dragon Knight'))
    ret_val.append(('drow-ranger', 'Drow Ranger'))
    ret_val.append(('earth-spirit', 'Earth Spirit'))
    ret_val.append(('earthshaker', 'Earthshaker'))
    ret_val.append(('elder-titan', 'Elder Titan'))
    ret_val.append(('ember-spirit', 'Ember Spirit'))
    ret_val.append(('enchantress', 'Enchantress'))
    ret_val.append(('enigma', 'Enigma'))
    ret_val.append(('faceless-void', 'Faceless Void'))
    ret_val.append(('grimstroke', 'Grimstroke'))
    ret_val.append(('gyrocopter', 'Gyrocopter'))
    ret_val.append(('huskar', 'Huskar'))
    ret_val.append(('invoker', 'Invoker'))
    ret_val.append(('io', 'Io'))
    ret_val.append(('jakiro', 'Jakiro'))
    ret_val.append(('juggernaut', 'Juggernaut'))
    ret_val.append(('keeper-of-the-light', 'Keeper of the Light'))
    ret_val.append(('kunkka', 'Kunkka'))
    ret_val.append(('legion-commander', 'Legion Commander'))
    ret_val.append(('leshrac', 'Leshrac'))
    ret_val.append(('lich', 'Lich'))
    ret_val.append(('lifestealer', 'Lifestealer'))
    ret_val.append(('lina', 'Lina'))
    ret_val.append(('lion', 'Lion'))
    ret_val.append(('lone-druid', 'Lone Druid'))
    ret_val.append(('luna', 'Luna'))
    ret_val.append(('lycan', 'Lycan'))
    ret_val.append(('magnus', 'Magnus'))
    ret_val.append(('mars', 'Mars'))
    ret_val.append(('medusa', 'Medusa'))
    ret_val.append(('meepo', 'Meepo'))
    ret_val.append(('mirana', 'Mirana'))
    ret_val.append(('monkey-king', 'Monkey King'))
    ret_val.append(('morphling', 'Morphling'))
    ret_val.append(('naga-siren', 'Naga Siren'))
    ret_val.append(('natures-prophet', 'Nature\'s Prophet'))
    ret_val.append(('necrophos', 'Necrophos'))
    ret_val.append(('night-stalker', 'Night Stalker'))
    ret_val.append(('nyx-assassin', 'Nyx Assassin'))
    ret_val.append(('ogre-magi', 'Ogre Magi'))
    ret_val.append(('omniknight', 'Omniknight'))
    ret_val.append(('oracle', 'Oracle'))
    ret_val.append(('outworld-devourer', 'Outworld Devourer'))
    ret_val.append(('pangolier', 'Pangolier'))
    ret_val.append(('phantom-assassin', 'Phantom Assassin'))
    ret_val.append(('phantom-lancer', 'Phantom Lancer'))
    ret_val.append(('phoenix', 'Phoenix'))
    ret_val.append(('puck', 'Puck'))
    ret_val.append(('pudge', 'Pudge'))
    ret_val.append(('pugna', 'Pugna'))
    ret_val.append(('queen-of-pain', 'Queen of Pain'))
    ret_val.append(('razor', 'Razor'))
    ret_val.append(('riki', 'Riki'))
    ret_val.append(('rubick', 'Rubick'))
    ret_val.append(('sand-king', 'Sand King'))
    ret_val.append(('shadow-demon', 'Shadow Demon'))
    ret_val.append(('shadow-fiend', 'Shadow Fiend'))
    ret_val.append(('shadow-shaman', 'Shadow Shaman'))
    ret_val.append(('silencer', 'Silencer'))
    ret_val.append(('skywrath-mage', 'Skywrath Mage'))
    ret_val.append(('slardar', 'Slardar'))
    ret_val.append(('slark', 'Slark'))
    ret_val.append(('snapfire', 'Snapfire'))
    ret_val.append(('sniper', 'Sniper'))
    ret_val.append(('spectre', 'Spectre'))
    ret_val.append(('spirit-breaker', 'Spirit Breaker'))
    ret_val.append(('storm-spirit', 'Storm Spirit'))
    ret_val.append(('sven', 'Sven'))
    ret_val.append(('techies', 'Techies'))
    ret_val.append(('templar-assassin', 'Templar Assassin'))
    ret_val.append(('terrorblade', 'Terrorblade'))
    ret_val.append(('tidehunter', 'Tidehunter'))
    ret_val.append(('timbersaw', 'Timbersaw'))
    ret_val.append(('tinker', 'Tinker'))
    ret_val.append(('tiny', 'Tiny'))
    ret_val.append(('treant-protector', 'Treant Protector'))
    ret_val.append(('troll-warlord', 'Troll Warlord'))
    ret_val.append(('tusk', 'Tusk'))
    ret_val.append(('underlord', 'Underlord'))
    ret_val.append(('undying', 'Undying'))
    ret_val.append(('ursa', 'Ursa'))
    ret_val.append(('vengeful-spirit', 'Vengeful Spirit'))
    ret_val.append(('venomancer', 'Venomancer'))
    ret_val.append(('viper', 'Viper'))
    ret_val.append(('visage', 'Visage'))
    ret_val.append(('void-spirit', 'Void Spirit'))
    ret_val.append(('warlock', 'Warlock'))
    ret_val.append(('weaver', 'Weaver'))
    ret_val.append(('windranger', 'Windranger'))
    ret_val.append(('winter-wyvern', 'Winter Wyvern'))
    ret_val.append(('witch-doctor', 'Witch Doctor'))
    ret_val.append(('wraith-king', 'Wraith King'))
    ret_val.append(('zeus', 'Zeus'))
    return ret_val

def save_mappings():
    all_heroes = get_mapping_list()
    with open('./mappings.json', 'w', encoding='utf-8') as f:
        json.dump(all_heroes, f, ensure_ascii=False, indent=4)

def main():
    header = create_header()
    all_heroes = get_mapping_list()
    counter = 0
    print('Starting to scrape...\n')
    for hero_element in all_heroes:
        url_fragment, hero_name = hero_element
        print('Working on: %s' %(hero_name))
        scrape_hero_info(url_fragment, hero_name,header)
        print('%s done' %(hero_name))
        counter += 1
        print ('%s/%s finished' %(str(counter), str(len(all_heroes))))
        time.sleep(3)



if __name__ == '__main__':
    main()