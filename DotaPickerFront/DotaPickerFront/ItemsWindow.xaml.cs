using DotaPickerFront.model;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
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
    /// Interaction logic for ItemsWindow.xaml
    /// </summary>
    public partial class ItemsWindow : Window, INotifyPropertyChanged
    {
        private ObservableCollection<Item> items;
        private ObservableCollection<Item> itemsSource;

        public ObservableCollection<Item> Items { get { return items; } set { if (value != items) { items = value; OnPropertyChanged("Items"); } } }

        public event PropertyChangedEventHandler PropertyChanged;
        protected virtual void OnPropertyChanged(string name)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }

        public ItemsWindow(List<Item> items)
        {
            InitializeComponent();
            DataContext = this;
            itemsSource = new ObservableCollection<Item>(items);
            Items = new ObservableCollection<Item>(items);
        }

        private void ItemIcon_MouseLeave(object sender, MouseEventArgs e)
        {

            Image heroImage = (Image)sender;
            heroImage.Width = 55;
            heroImage.Height = 40;
            heroImage.Margin = new Thickness(5, 3, 5, 3);

        }

        private void ItemIcon_MouseEnter(object sender, MouseEventArgs e)
        {
            Image heroImage = (Image)sender;
            heroImage.Width = 65;
            heroImage.Height = 46;
            heroImage.Margin = new Thickness(0, 0, 0, 0);
        }

        private void FilterTextChanged(object sender, TextChangedEventArgs e)
        {
            string theText = "";
            TextBox textBox = sender as TextBox;
            if (textBox != null)
            {
                theText = textBox.Text;
            }
            Items = new ObservableCollection<Item>(itemsSource.Where(item => item.Name.ToLower().Contains(theText.ToLower())));
        }
    }
}
