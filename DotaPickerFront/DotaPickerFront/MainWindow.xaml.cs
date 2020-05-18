using DotaPickerFront.ultility;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Web.Script.Serialization;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Xml.Serialization;
using Image = System.Windows.Controls.Image;

namespace DotaPickerFront
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

        private List<List<string>> mappings;
        private Dictionary<string, string> nameToId;
        public Dictionary<string, BitmapImage> images;
        public Dictionary<string, FormatConvertedBitmap> grayImages;
        
        private Dictionary<string, Image> controlIcons;

        public Dictionary<string, object> Heroes { get; set; }
        public List<string> HeroPreferences { get; set; }
        public List<string> LanePreferences { get; set; }
        public List<string> RolePreferences { get; set; }

        private List<string> allies;
        private List<string> enemies;

        private static readonly HttpClient client = new HttpClient();

        public MainWindow()
        {
            InitializeComponent();

            controlIcons = new Dictionary<string, Image>();
            images = new Dictionary<string, BitmapImage>();
            grayImages = new Dictionary<string, FormatConvertedBitmap>();
            allies = new List<string>();
            enemies = new List<string>();

            HeroPreferences = new List<string>();
            LanePreferences = new List<string>();
            RolePreferences = new List<string>();

            LoadPreferences();

            LoadFiles();
            PopulateHeroGrid();
        }

        private void LoadPreferences()
        {
            if (File.Exists("rolePreferences.xml"))
                RolePreferences = DeserializeList("rolePreferences.xml");
            else
                SerializeList(RolePreferences, "rolePreferences.xml");
            if (File.Exists("lanePreferences.xml"))
                LanePreferences = DeserializeList("lanePreferences.xml");
            else
                SerializeList(LanePreferences, "lanePreferences.xml");
            if (File.Exists("heroPreferences.xml"))
                HeroPreferences = DeserializeList("heroPreferences.xml");
            else
                SerializeList(HeroPreferences, "heroPreferences.xml");
        }

        public void SerializeList(List<string> toSerialize, string fileName)
        {
            var serializer = new XmlSerializer(typeof(List<string>));
            using (var stream = File.Open(fileName, FileMode.Create))
            {
                serializer.Serialize(stream, toSerialize);
            }
        }


        public List<string> DeserializeList(string fileName)
        {
            var serializer = new XmlSerializer(typeof(List<string>));
            using (var stream = File.OpenRead(fileName))
            {
                return (List<string>)serializer.Deserialize(stream);
            }
        }



        private void LoadFiles()
        {
            mappings = FileLoader.LoadMapings();
            nameToId = new Dictionary<string, string>();
            mappings.ForEach(mapping => nameToId[mapping[1]] = mapping[0]);

            var images = FileLoader.LoadImages(mappings);
            foreach (KeyValuePair<string, Bitmap> entry in images)
            {
                this.images[entry.Key] = BitmapConverter.BitmapToImageSource(entry.Value);
                grayImages[entry.Key] = BitmapConverter.BitmapImageToGraycale(this.images[entry.Key]);
            }


            Heroes = FileLoader.LoadHeroes(mappings);
        }

        private void PopulateHeroGrid()
        {
            List<string> agility = FilterByPrimaryAttribute("agility");
            List<string> strength = FilterByPrimaryAttribute("strength");
            List<string> intelligence = FilterByPrimaryAttribute("intelligence");

            PopulateAttributeGrid(AgilityHeroesGrid, 7, 7, agility);
            PopulateAttributeGrid(StrengthHeroesGrid, 7, 7, strength);
            PopulateAttributeGrid(IntelligenceHeroesGrid, 7, 7, intelligence);
        }

        private void PopulateAttributeGrid(Grid grid, int rows, int columns, List<string> heroes)
        {
            for (int i = 0; i < rows; i++)
            {
                grid.RowDefinitions.Add(new RowDefinition());
            }
            for (int i = 0; i < columns; i++)
            {
                grid.ColumnDefinitions.Add(new ColumnDefinition());
            }

            for (int i = 0; i < heroes.Count; i++)
            {
                string hero = heroes[i];
                Image heroIcon = new Image();
                heroIcon.Source = images[hero];
                heroIcon.Margin = new Thickness(5, 2, 5, 2);

                heroIcon.Tag = hero;
                heroIcon.MouseLeftButtonUp += addHeroToAllies;
                heroIcon.MouseRightButtonUp += addHeroToEnemies;
                heroIcon.MouseEnter += HeroIcon_MouseEnter;
                heroIcon.MouseLeave += HeroIcon_MouseLeave;

                grid.Children.Add(heroIcon);
                Grid.SetRow(heroIcon, i / columns);
                Grid.SetColumn(heroIcon, i % columns);
                controlIcons[hero] = heroIcon;
            }
        }


        private void HeroIcon_MouseLeave(object sender, MouseEventArgs e)
        {
            Image heroImage = (Image)sender;
            heroImage.Margin = new Thickness(5, 2, 5, 2);
        }

        private void HeroIcon_MouseEnter(object sender, MouseEventArgs e)
        {
            Image heroImage = (Image)sender;
            heroImage.Margin = new Thickness(0, 0, 0, 0);
        }

        private void addHeroToAllies(object sender, MouseButtonEventArgs e)
        {
            Image heroImage = (Image)sender;
            string hero = (string)heroImage.Tag;

            if (allies.Count >= 4 || allies.Contains(hero) || enemies.Contains(hero))
            {
                return;
            }

            controlIcons[hero].Source = grayImages[hero];

            int column = allies.Count;
            allies.Add(hero);

            Image selectedHeroIcon = new Image();
            selectedHeroIcon.Source = images[hero];
            selectedHeroIcon.Tag = heroImage.Tag;
            SelectedHeroesGrid.Children.Add(selectedHeroIcon);
            Grid.SetRow(selectedHeroIcon, 0);
            Grid.SetColumn(selectedHeroIcon, column);
        }

        private void addHeroToEnemies(object sender, MouseButtonEventArgs e)
        {
            Image heroImage = (Image)sender;
            string hero = (string)heroImage.Tag;

            if (enemies.Count >= 5 || allies.Contains(hero) || enemies.Contains(hero))
            {
                return;
            }

            controlIcons[hero].Source = grayImages[hero];

            int column = enemies.Count + 6;
            enemies.Add(hero);

            Image selectedHeroIcon = new Image();
            selectedHeroIcon.Margin = new Thickness(5, 0, 5, 0);
            selectedHeroIcon.Source = images[hero];
            selectedHeroIcon.Tag = heroImage.Tag;
            SelectedHeroesGrid.Children.Add(selectedHeroIcon);
            Grid.SetRow(selectedHeroIcon, 0);
            Grid.SetColumn(selectedHeroIcon, column);
        }

        private List<string> FilterByPrimaryAttribute(string primaryAttribute)
        {
            List<string> retVal = new List<string>();
            foreach (KeyValuePair<string, object> entry in Heroes)
            {
                Dictionary<string, object> heroDict = (Dictionary<string, object>)entry.Value;
                if (primaryAttribute == (string)heroDict["primary_attribute"])
                {
                    retVal.Add(entry.Key);
                }
            }
            return retVal;
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            PreferenceWindow preferenceWindow = new PreferenceWindow(this);
            preferenceWindow.ShowDialog();
        }

        private List<string> heroNamesToIds(List<string> heroNames)
        {
            return heroNames.Select(heroName => nameToId[heroName]).ToList();
        }

        private async void ShowButton_Click(object sender, RoutedEventArgs e)
        {
            var postDict = new Dictionary<string, object>();
            postDict["heroPreferences"] = heroNamesToIds(HeroPreferences);
            postDict["lanePreferences"] = LanePreferences;
            postDict["rolePreferences"] = RolePreferences;
            postDict["allies"] = allies;
            postDict["enemies"] = enemies;
            var content = new JavaScriptSerializer().Serialize(postDict);
            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Post, "http://localhost:8080/api/pick");
            request.Content = new StringContent(content, Encoding.UTF8, "application/json");
            var response = await client.SendAsync(request);
            var responseString = await response.Content.ReadAsStringAsync();
            List<Dictionary<string, object>> recommendations = new JavaScriptSerializer().Deserialize<List<Dictionary<string, object>>>(responseString);

            ResultWindow resultWindow = new ResultWindow(recommendations, this);
            resultWindow.ShowDialog();
            
        }
    }
}
