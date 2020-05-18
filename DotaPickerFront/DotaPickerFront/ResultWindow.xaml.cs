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

namespace DotaPickerFront
{
    /// <summary>
    /// Interaction logic for ResultWindow.xaml
    /// </summary>
    public partial class ResultWindow : Window
    {
        private MainWindow _mainWindow;
        private List<Dictionary<string, object>> _results;

        public ResultWindow(List<Dictionary<string, object>> results, MainWindow mainWindow)
        {
            InitializeComponent();

            _mainWindow = mainWindow;
            _results = results;

            DisplayResults(15);
        }

        private void DisplayResults(int toDisplay)
        {
            for (int i = 0; i < toDisplay; i++)
            {
                ResultsGrid.RowDefinitions.Add(new RowDefinition());
            }
            for (int i = 0; i < toDisplay; i++)
            {
                Image heroIcon = new Image();
                heroIcon.Margin = new Thickness(5, 5, 5, 5);

                Dictionary<string, object> heroDict = (Dictionary<string, object>)_results[i]["hero"];
                string heroId = (string)heroDict["heroId"];
                string heroName = (string)heroDict["heroName"];

                heroIcon.Source = _mainWindow.images[heroId];
   
                ResultsGrid.Children.Add(heroIcon);

                Label heroLabel = new Label();
                heroLabel.Content = heroName;
                ResultsGrid.Children.Add(heroLabel);
                Grid.SetColumn(heroLabel, 1);
                Grid.SetRow(heroLabel, i);


                Grid.SetColumn(heroIcon, 0);
                Grid.SetRow(heroIcon, i);

                Label heroScore = new Label();
                heroScore.Content = _results[i]["score"];

                ResultsGrid.Children.Add(heroScore);

                Grid.SetColumn(heroScore, 2);
                Grid.SetRow(heroScore, i);

            }
        }

    }
}
