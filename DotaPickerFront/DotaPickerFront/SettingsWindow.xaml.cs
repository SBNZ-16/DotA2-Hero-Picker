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
using MaterialDesignThemes.Wpf;

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

        public SnackbarMessageQueue MyCustomMessageQueue { get; set; }

        private static readonly HttpClient client = new HttpClient();

        public SettingsWindow(SettingsStats settingsStats)
        {
            InitializeComponent();
            SettingsStats = settingsStats;
            DataContext = this;
            MyCustomMessageQueue = new SnackbarMessageQueue(TimeSpan.FromMilliseconds(1000));
        }


        private async void SaveChanges(object sender, RoutedEventArgs e)
        {
            var content = new JavaScriptSerializer().Serialize(SettingsStats);
            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Post, "http://localhost:8080/api/settings");
            request.Content = new StringContent(content, Encoding.UTF8, "application/json");
            try
            {
                var response = await client.SendAsync(request);
                var responseString = await response.Content.ReadAsStringAsync();
                MyCustomMessageQueue.Enqueue(responseString);
            }
            catch (Exception)
            {
                MyCustomMessageQueue.Enqueue("Error in communication with a server");
            }
        }

        private void ResetToVanillaRules(object sender, RoutedEventArgs e)
        {
            SettingsStats.RulesTemplate = SettingsStats.VanillaRulesTemplate;
        }
    }
}
