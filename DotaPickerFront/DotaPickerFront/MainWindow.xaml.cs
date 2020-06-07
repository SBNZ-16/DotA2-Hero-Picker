using DotaPickerFront.model;
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
using System.Windows.Controls.Primitives;
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

        public Dictionary<string, BitmapImage> avatarImages;

        private Dictionary<string, Image> controlIcons;
        private List<Image> allyIcons;
        private List<Image> enemyIcons;
        private List<LaneToggle> enemyLaneToggles;

        public Dictionary<string, object> Heroes { get; set; }
        public List<string> HeroPreferences { get; set; }
        public List<string> LanePreferences { get; set; }
        public List<string> RolePreferences { get; set; }

        private List<string> allies;
        private List<string> enemies;
        private List<string> banned;

        private static readonly HttpClient client = new HttpClient();

        public MainWindow()
        {
            InitializeComponent();

            controlIcons = new Dictionary<string, Image>();
            allyIcons = new List<Image>();
            enemyIcons = new List<Image>();
            enemyLaneToggles = new List<LaneToggle>();

            images = new Dictionary<string, BitmapImage>();
            grayImages = new Dictionary<string, FormatConvertedBitmap>();
            avatarImages = new Dictionary<string, BitmapImage>();
            allies = new List<string>();
            enemies = new List<string>();
            banned = new List<string>();

            HeroPreferences = new List<string>();
            LanePreferences = new List<string>();
            RolePreferences = new List<string>();

            LoadPreferences();

            LoadFiles();
            PopulateHeroGrid();
            PopulateSelectedHeroes();
        }


        private void PopulateSelectedHeroes()
        {
            for (int i = 0; i < 4; i++)
            {
                Image allyHeroImage = new Image();
                allyHeroImage.Margin = new Thickness(5, 0, 5, 0);
                allyHeroImage.Tag = "";
                allyHeroImage.MouseLeftButtonUp += UnselectHero;

                SelectedHeroesGrid.Children.Add(allyHeroImage);
                Grid.SetRow(allyHeroImage, 0);
                Grid.SetColumn(allyHeroImage, i);
                allyIcons.Add(allyHeroImage);
            }
            redrawAllies();

            Image playerImage = new Image();
            playerImage.Margin = new Thickness(5, 0, 5, 0);
            playerImage.Source = avatarImages["player_avatar"];
            playerImage.Tag = "";

            SelectedHeroesGrid.Children.Add(playerImage);
            Grid.SetRow(playerImage, 0);
            Grid.SetColumn(playerImage, 4);


            StackPanel stackPanel = new StackPanel();
            stackPanel.VerticalAlignment = VerticalAlignment.Center;
            stackPanel.HorizontalAlignment = HorizontalAlignment.Center;
            Label vsLabel = new Label();
            vsLabel.Content = "VS";
            vsLabel.FontSize = 40;
            stackPanel.Children.Add(vsLabel);

            SelectedHeroesGrid.Children.Add(stackPanel);
            Grid.SetRow(stackPanel, 0);
            Grid.SetColumn(stackPanel, 5);

            for (int i = 0; i < 5; i++)
            {
                Image enemyHeroImage = new Image();
                enemyHeroImage.Margin = new Thickness(5, 0, 5, 0);
                enemyHeroImage.Tag = "";
                enemyHeroImage.MouseLeftButtonUp += UnselectHero;

                SelectedHeroesGrid.Children.Add(enemyHeroImage);
                Grid.SetRow(enemyHeroImage, 0);
                Grid.SetColumn(enemyHeroImage, i + 6);

                enemyIcons.Add(enemyHeroImage);
            }
            redrawEnemies();
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

            var avatarMappings = new List<List<string>>()
            {
                new List<string> {"ally_avatar"},
                new List<string> {"enemy_avatar"},
                new List<string> {"player_avatar"}
            };
            var avatarImages = FileLoader.LoadImages(avatarMappings);
            foreach (KeyValuePair<string, Bitmap> entry in avatarImages)
            {
                this.avatarImages[entry.Key] = BitmapConverter.BitmapToImageSource(entry.Value);
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
                heroIcon.Margin = new Thickness(5, 0, 5, 0);

                heroIcon.Tag = hero;
                heroIcon.MouseLeftButtonUp += addHeroToAllies;
                heroIcon.MouseRightButtonUp += addHeroToEnemies;
                heroIcon.MouseDown += ChangeBanStatus;
                heroIcon.MouseEnter += HeroIcon_MouseEnter;
                heroIcon.MouseLeave += HeroIcon_MouseLeave;

                grid.Children.Add(heroIcon);
                Grid.SetRow(heroIcon, i / columns);
                Grid.SetColumn(heroIcon, i % columns);
                controlIcons[hero] = heroIcon;
            }
        }

        private void ChangeBanStatus(object sender, MouseButtonEventArgs e)
        {
            if (e.MiddleButton == MouseButtonState.Pressed)
            {
                Image heroImage = (Image)sender;
                string hero = (string)heroImage.Tag;
                if (!allies.Contains(hero) && !enemies.Contains(hero))
                {
                    Grid grid = determineGrid(hero);
                    if (!banned.Contains(hero))
                    {
                        drawRedEllipse(heroImage, grid);
                        banned.Add(hero);
                    }
                    else
                    {
                        removeRedEllipse(heroImage, grid);
                        banned.Remove(hero);
                    }
                }
            }
        }

        private Grid determineGrid(string hero)
        {
            string attribute = (string)((Dictionary<string, object>)Heroes[hero])["primary_attribute"];
            if (attribute == "agility")
            {
                return AgilityHeroesGrid;
            }
            else if (attribute == "strength")
            {
                return StrengthHeroesGrid;
            }
            else
            {
                return IntelligenceHeroesGrid;
            }
        }

        private void drawRedEllipse(Image heroImage, Grid grid)
        {
            Canvas canvas = new Canvas();
            Ellipse ellipse = new Ellipse();
            ellipse.Fill = System.Windows.Media.Brushes.Red;
            ellipse.Width = 25;
            ellipse.Height = 25;
            ellipse.StrokeThickness = 2;

            int row = Grid.GetRow(heroImage);
            int columnn = Grid.GetColumn(heroImage);

            canvas.Children.Add(ellipse);

            double left = (canvas.ActualWidth - ellipse.ActualWidth) / 2;
            Canvas.SetLeft(ellipse, left);

            double top = (canvas.ActualHeight - ellipse.ActualHeight) / 2;
            Canvas.SetTop(ellipse, top);

            grid.Children.Add(canvas);

            Grid.SetColumn(canvas, columnn);
            Grid.SetRow(canvas, row);
        }

        private void removeRedEllipse(Image heroImage, Grid grid)
        {
            int row = Grid.GetRow(heroImage);
            int column = Grid.GetColumn(heroImage);

            UIElement toRemove = grid.Children.Cast<UIElement>().Where(e => Grid.GetRow(e) == row && Grid.GetColumn(e) == column).ToList()[1];
            grid.Children.Remove(toRemove);
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

            if (allies.Count >= 4 || allies.Contains(hero) || enemies.Contains(hero) || banned.Contains(hero))
            {
                return;
            }

            controlIcons[hero].Source = grayImages[hero];

            int column = allies.Count;
            allies.Add(hero);

            Image selectedHeroIcon = allyIcons[column];
            selectedHeroIcon.Source = images[hero];
            selectedHeroIcon.Tag = heroImage.Tag;
        }

        private void addHeroToEnemies(object sender, MouseButtonEventArgs e)
        {
            Image heroImage = (Image)sender;
            string hero = (string)heroImage.Tag;

            if (enemies.Count >= 5 || allies.Contains(hero) || enemies.Contains(hero) || banned.Contains(hero))
            {
                return;
            }

            controlIcons[hero].Source = grayImages[hero];

            int column = enemies.Count;
            enemies.Add(hero);

            Image selectedHeroIcon = enemyIcons[column];
            selectedHeroIcon.Source = images[hero];
            selectedHeroIcon.Tag = heroImage.Tag;

            LaneToggle laneToggle = new LaneToggle();
            SelectedHeroesGrid.Children.Add(laneToggle);
            Grid.SetRow(laneToggle, 1);
            Grid.SetColumn(laneToggle, column + 6);
            laneToggle.Margin = new Thickness(0, 5, 0, 0);
            enemyLaneToggles.Add(laneToggle);
        }

        private void UnselectHero(object sender, MouseButtonEventArgs e)
        {
            Image heroImage = (Image)sender;
            string hero = (string)heroImage.Tag;
            if (hero != "") // "" is for avatars
            {
                if (allies.Contains(hero))
                {
                    allies.Remove(hero);
                    redrawAllies();
                }
                else
                {
                    int index = enemies.IndexOf(hero);
                    enemies.Remove(hero);

                    LaneToggle laneToggle = enemyLaneToggles[index];
                    enemyLaneToggles.Remove(laneToggle);
                    SelectedHeroesGrid.Children.Remove(laneToggle);

                    redrawEnemies();
                }
                controlIcons[hero].Source = images[hero];
            }
        }

        private void redrawAllies()
        {
            for (int i = 0; i < 4; i++)
            {
                if (i < allies.Count)
                {
                    string hero = allies[i];
                    allyIcons[i].Tag = hero;
                    allyIcons[i].Source = images[hero];
                }
                else
                {
                    string hero = "";
                    allyIcons[i].Tag = hero;
                    allyIcons[i].Source = avatarImages["ally_avatar"];
                }
            }

        }

        private void redrawEnemies()
        {
            for (int i = 0; i < 5; i++)
            {
                if (i < enemies.Count)
                {
                    string hero = enemies[i];
                    enemyIcons[i].Tag = hero;
                    enemyIcons[i].Source = images[hero];
                }
                else
                {
                    string hero = "";
                    enemyIcons[i].Tag = hero;
                    enemyIcons[i].Source = avatarImages["enemy_avatar"];
                }
            }
            for (int i = 0; i < enemyLaneToggles.Count; i++)
            {
                Grid.SetColumn(enemyLaneToggles[i], i + 6);
            }
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
            postDict["enemyLanes"] = enemyLaneToggles.Select(x => ((ToggleButton)x.FindName("toggleButton")).IsChecked).ToList();
            postDict["banned"] = banned;
            var content = new JavaScriptSerializer().Serialize(postDict);
            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Post, "http://localhost:8080/api/pick");
            request.Content = new StringContent(content, Encoding.UTF8, "application/json");
            try
            {
                var response = await client.SendAsync(request);
                var responseString = await response.Content.ReadAsStringAsync();
                List<Dictionary<string, object>> recommendations = new JavaScriptSerializer().Deserialize<List<Dictionary<string, object>>>(responseString);

                ResultWindow resultWindow = new ResultWindow(recommendations, this);
                resultWindow.ShowDialog();
            }
            catch (Exception ex)
            {
                return;
            }
        }

        private async void SettingsButtonClick(object sender, RoutedEventArgs e)
        {
            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, "http://localhost:8080/api/settings");
            try
            {
                var response = await client.SendAsync(request);
                var responseString = await response.Content.ReadAsStringAsync();
                SettingsStats settingsStats = new JavaScriptSerializer().Deserialize<SettingsStats>(responseString);

                SettingsWindow settingsWindow = new SettingsWindow(settingsStats);
                settingsWindow.ShowDialog();
            }
            catch (Exception ex)
            {
                return;
            }

        }
    }
}
