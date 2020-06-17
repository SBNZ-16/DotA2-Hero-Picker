import json
import requests
import time
from bs4 import BeautifulSoup
import bs4
from PIL import Image
from io import BytesIO


# def main():
#     new_json = []
#     with open('data.json') as json_file:
#         data = json.load(json_file)
#         for item in data:
#             new_item = {}
#             new_item["tag"] = item["tag"]
#             new_item["name"] = item["name"]
#             new_item["cost"] = item["cost"]
#             if "components" in item:
#
#
#
#         print(data[55].keys())

def find_items_suffixes_by_categories(soup, categories):
    retval = []
    for cat in categories:
        result = soup.find(id=cat)
        for item_link_suffix in result.parent.find_next_sibling().children:
            retval.append(item_link_suffix.a["href"])
    return retval


def save_img(url, name):
    image_response = requests.get(url)
    bytes = BytesIO(image_response.content)
    image = Image.open(bytes)
    image.save('./icons/%s.png' % (name))


def scrape_item(url, item_suffix):
    item = {'name': None, 'components': [], 'cost': None}
    retval = [item]
    htmlContent = requests.get(url + item_suffix).content
    soup = BeautifulSoup(htmlContent, features="html.parser")
    a = soup.find("a", {"href": item_suffix})
    name_and_price = a.children.__next__()['alt'].rsplit('(', 1)
    item['name'] = name_and_price[0].strip()
    item['cost'] = int(name_and_price[1][:-1].strip())
    save_img(soup.find(id="itemmainimage").a.img['src'], item['name'])

    if a.find_next_siblings():
        for component_div in a.find_next_siblings()[1].div.children:
            if type(component_div) == bs4.element.Tag:
                name_and_price = component_div.a['title'].rsplit('(', 1)
                component_name = name_and_price[0].strip()
                if component_name == 'Recipe':
                    component_name = 'Recipe: ' + item['name']
                    component_cost = int(name_and_price[1][:-1].strip())
                    retval.append({'name': component_name, 'components': [], 'cost': component_cost})
                item['components'].append(component_name)
    return retval


def main():
    url = 'https://dota2.gamepedia.com'
    htmlContent = requests.get(url + "/Items").content
    soup = BeautifulSoup(htmlContent, features="html.parser")
    item_suffixes = find_items_suffixes_by_categories(soup, ["Attributes", "Equipment", "Miscellaneous", "Secret",
                                                             "Accessories", "Support", "Magical", "Armor", "Weapons",
                                                             "Artifacts"])
    items = []
    for item_suffix in item_suffixes:
        print('Working on: %s' % (item_suffix))
        items += scrape_item(url, item_suffix)
        print('%s done' % (item_suffix))

    with open('./items.json', 'w', encoding='utf-8') as f:
        json.dump(items, f, ensure_ascii=False, indent=4)


if __name__ == '__main__':
    main()
    # print(scrape_item('https://dota2.gamepedia.com', '/Eul%27s_Scepter_of_Divinity'))
    # print(scrape_item('https://dota2.gamepedia.com', '/Staff_of_Wizardry'))
