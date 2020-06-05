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

namespace DotaPickerFront
{
    /// <summary>
    /// Interaction logic for SettingsWindow.xaml
    /// </summary>
    public partial class SettingsWindow : Window
    {
        private static readonly HttpClient client = new HttpClient();

        public SettingsWindow()
        {
            InitializeComponent();

            LoadData();
        }

        private async void LoadData()
        {
            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, "http://localhost:8080/api/settings");
            try
            {
                var response = await client.SendAsync(request);
                var responseString = await response.Content.ReadAsStringAsync();
                List<Dictionary<string, object>> recommendations = new JavaScriptSerializer().Deserialize<List<Dictionary<string, object>>>(responseString);

                
            }
            catch (Exception ex)
            {
                return;
            }
        }

        private void SaveChanges(object sender, RoutedEventArgs e)
        {

        }
    }
}
