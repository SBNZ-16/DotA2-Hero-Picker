using System;
using System.Collections.Generic;
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
using System.Net.Http;
using System.Web.Script.Serialization;
using DotaPickerFront.model;
using System.ComponentModel;

namespace DotaPickerFront
{
    /// <summary>
    /// Interaction logic for SettingsWindow.xaml
    /// </summary>
    public partial class SettingsWindow : Window, INotifyPropertyChanged
    {
        private SettingsStats settingsStats;

        public event PropertyChangedEventHandler PropertyChanged;
        protected virtual void OnPropertyChanged(string name)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }

        public SettingsStats SettingsStats { get { return settingsStats; } set { if (value != settingsStats) { settingsStats = value; OnPropertyChanged("SettingsStats"); } } }

        public SettingsWindow(SettingsStats settingsStats)
        {
            SettingsStats = settingsStats;
            InitializeComponent();
        }


        private void SaveChanges(object sender, RoutedEventArgs e)
        {

        }
    }
}
