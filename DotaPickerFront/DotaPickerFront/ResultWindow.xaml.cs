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
using System.Windows.Media.Animation;
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
            var sb = new Storyboard();
            for (int i = 0; i < toDisplay; i++)
            {
                Image heroIcon = new Image();
                heroIcon.Margin = new Thickness(5, 5, 5, 5);

                Dictionary<string, object> heroDict = (Dictionary<string, object>)_results[i]["hero"];
                string heroId = (string)heroDict["heroId"];
                string heroName = (string)heroDict["heroName"];

                heroIcon.Source = _mainWindow.images[heroId];
   
                ResultsGrid.Children.Add(heroIcon);
                Grid.SetColumn(heroIcon, 0);
                Grid.SetRow(heroIcon, i);

                Label heroLabel = new Label();
                heroLabel.FontSize = 20;
                heroLabel.Content = heroName;

                StackPanel stackPanel = new StackPanel();
                stackPanel.Children.Add(heroLabel);
                stackPanel.VerticalAlignment = VerticalAlignment.Center;

                ResultsGrid.Children.Add(stackPanel);
                Grid.SetColumn(stackPanel, 1);
                Grid.SetRow(stackPanel, i);


                

                double score = Double.Parse(_results[i]["score"].ToString());

                ProgressBar progressBar = new ProgressBar();
                progressBar.Margin = new Thickness(10, 15, 10, 15);
                ResultsGrid.Children.Add(progressBar);
                Grid.SetColumn(progressBar, 2);
                Grid.SetRow(progressBar, i);

                var fillUp = new DoubleAnimation()
                {
                    From = 0,
                    To = score,
                    Duration = TimeSpan.FromSeconds(2),
                };

                Storyboard.SetTarget(fillUp, progressBar);
                Storyboard.SetTargetProperty(fillUp, new PropertyPath(ProgressBar.ValueProperty));
                sb.Children.Add(fillUp);
            }
            sb.Begin();
        }

    }
}
