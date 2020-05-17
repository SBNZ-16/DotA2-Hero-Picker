using DotaPickerFront.ultility;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Image = System.Windows.Controls.Image;

namespace DotaPickerFront
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

        private List<List<string>> mappings;
        private Dictionary<string, BitmapImage> images;
        private Dictionary<string, FormatConvertedBitmap> grayImages;
        private Dictionary<string, object> heroes;
        private Dictionary<string, Image> controlIcons;

        private List<string> allies;
        private List<string> enemies;

        public MainWindow()
        {
            InitializeComponent();

            controlIcons = new Dictionary<string, Image>();
            images = new Dictionary<string, BitmapImage>();
            grayImages = new Dictionary<string, FormatConvertedBitmap>();
            allies = new List<string>();
            enemies = new List<string>();


            LoadFiles();
            PopulateHeroGrid();
        }

        private void LoadFiles()
        {
            mappings = FileLoader.LoadMapings();

            var images = FileLoader.LoadImages(mappings);
            foreach (KeyValuePair<string, Bitmap> entry in images)
            {
                this.images[entry.Key] = BitmapConverter.BitmapToImageSource(entry.Value);
                grayImages[entry.Key] = BitmapConverter.BitmapImageToGraycale(this.images[entry.Key]);
            }


            heroes = FileLoader.LoadHeroes(mappings);
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
            foreach (KeyValuePair<string, object> entry in heroes)
            {
                Dictionary<string, object> heroDict = (Dictionary<string, object>)entry.Value;
                if (primaryAttribute == (string)heroDict["primary_attribute"])
                {
                    retVal.Add(entry.Key);
                }
            }
            return retVal;
        }
    }
}
