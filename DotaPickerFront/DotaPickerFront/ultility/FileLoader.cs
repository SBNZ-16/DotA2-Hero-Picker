using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Script.Serialization;

namespace DotaPickerFront.ultility
{
    class FileLoader
    {

        public static List<List<string>> LoadMapings()
        {
            string mappingsPath = "../../resources/mappings.json";
            string json = File.ReadAllText(mappingsPath);
            List<List<string>> retVal = new JavaScriptSerializer().Deserialize<List<List<string>>>(json);
            return retVal;
        }

        public static Dictionary<string, Bitmap> LoadImages(List<List<string>> mappings)
        {
            Dictionary<string, Bitmap> retVal = new Dictionary<string, Bitmap>();
            foreach (var element in mappings)
            {
                string heroId = element[0];
                string jpgPath = $"../../resources/icons/{heroId}.jpg";
                retVal[heroId] = new Bitmap(Image.FromFile(jpgPath));
            }
            return retVal;
        }

        public static Dictionary<string, object> LoadHeroes(List<List<string>> mappings)
        {
            Dictionary<string, object> retVal = new Dictionary<string, object>();
            foreach (var element in mappings)
            {
                string heroId = element[0];
                string jsonPath = $"../../resources/hero_data/{heroId}.json";
                string json = File.ReadAllText(jsonPath);
                Dictionary<string, object> heroDict = new JavaScriptSerializer().Deserialize<Dictionary<string, object>>(json);
                retVal[heroId] = heroDict;
            }
            return retVal;
        }

    }
}
