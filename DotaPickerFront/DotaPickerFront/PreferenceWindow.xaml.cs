using System;
using System.Collections.Generic;
using System.IO;
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
using System.Windows.Shapes;
using System.Xml.Serialization;

namespace DotaPickerFront
{
    /// <summary>
    /// Interaction logic for PreferenceWindow.xaml
    /// </summary>
    public partial class PreferenceWindow : Window
    {

        private MainWindow _mainWindow;
        private List<string> AllHeroes;

        public PreferenceWindow(MainWindow window)
        {
            InitializeComponent();
            _mainWindow = window;


            GetAllHeroNames();
            LoadPreferences();
        }

        private void GetAllHeroNames()
        {
           AllHeroes = _mainWindow.Heroes.Values.Select(x => (string)(((Dictionary<string, object>)x))["name"]).ToList();
           HeroesListBox.ItemsSource = AllHeroes;
        }


        private void LoadPreferences()
        {
            if (_mainWindow.LanePreferences.Contains("Off Lane"))
                OffLaneCheckbox.IsChecked = true;
            if (_mainWindow.LanePreferences.Contains("Mid Lane"))
                MidLaneCheckbox.IsChecked = true;
            if (_mainWindow.LanePreferences.Contains("Safe Lane"))
                SafeLaneCheckbox.IsChecked = true;
            if (_mainWindow.LanePreferences.Contains("Jungle"))
                JungleCheckbox.IsChecked = true;
            if (_mainWindow.LanePreferences.Contains("Roaming"))
                RoamingCheckbox.IsChecked = true;

            if (_mainWindow.RolePreferences.Contains("Nuker"))
                NukerCheckbox.IsChecked = true;
            if (_mainWindow.RolePreferences.Contains("Escape"))
                EscapeCheckbox.IsChecked = true;
            if (_mainWindow.RolePreferences.Contains("Support"))
                SupportCheckbox.IsChecked = true;
            if (_mainWindow.RolePreferences.Contains("Initiator"))
                InitiatorCheckbox.IsChecked = true;
            if (_mainWindow.RolePreferences.Contains("Carry"))
                CarryCheckbox.IsChecked = true;
            if (_mainWindow.RolePreferences.Contains("Jungler"))
                JunglerCheckbox.IsChecked = true;
            if (_mainWindow.RolePreferences.Contains("Durable"))
                DurableCheckbox.IsChecked = true;
            if (_mainWindow.RolePreferences.Contains("Disabler"))
                DisablerCheckbox.IsChecked = true;
            if (_mainWindow.RolePreferences.Contains("Pusher"))
                PusherCheckbox.IsChecked = true;

            _mainWindow.HeroPreferences.ForEach(heroPref => HeroesListBox.SelectedItems.Add(heroPref));

            _mainWindow.RolePreferences = _mainWindow.RolePreferences.Distinct().ToList();
            _mainWindow.LanePreferences = _mainWindow.LanePreferences.Distinct().ToList();
        }

        private void CloseButton_Click(object sender, RoutedEventArgs e)
        {
            Close();
        }

        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            _mainWindow.RolePreferences = _mainWindow.RolePreferences.Distinct().ToList();
            _mainWindow.LanePreferences = _mainWindow.LanePreferences.Distinct().ToList();
            _mainWindow.HeroPreferences = new List<string>();
            foreach (var heroItem in HeroesListBox.SelectedItems)
            {
                _mainWindow.HeroPreferences.Add((string)heroItem);
            }

            _mainWindow.SerializeList(_mainWindow.RolePreferences, "rolePreferences.xml");
            _mainWindow.SerializeList(_mainWindow.LanePreferences, "lanePreferences.xml");
            _mainWindow.SerializeList(_mainWindow.HeroPreferences, "heroPreferences.xml");



        }



        private void Lane_Unchecked(object sender, RoutedEventArgs e)
        {
            _mainWindow.LanePreferences = _mainWindow.LanePreferences.Distinct().ToList();
            _mainWindow.LanePreferences.Remove(((CheckBox)sender).Content.ToString());
        }

        private void Lane_Checked(object sender, RoutedEventArgs e)
        {
            _mainWindow.LanePreferences = _mainWindow.LanePreferences.Distinct().ToList();
            _mainWindow.LanePreferences.Add(((CheckBox)sender).Content.ToString());
        }

        private void Role_Unchecked(object sender, RoutedEventArgs e)
        {
            _mainWindow.RolePreferences = _mainWindow.RolePreferences.Distinct().ToList();
            _mainWindow.RolePreferences.Remove(((CheckBox)sender).Content.ToString());
        }

        private void Role_Checked(object sender, RoutedEventArgs e)
        {
             _mainWindow.RolePreferences = _mainWindow.RolePreferences.Distinct().ToList();
            _mainWindow.RolePreferences.Add(((CheckBox)sender).Content.ToString());
        }
    }
}
