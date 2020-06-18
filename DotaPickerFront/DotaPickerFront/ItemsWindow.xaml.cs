using DotaPickerFront.model;
using System;
using System.Collections.Generic;
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
        private List<Item> items;
        public List<Item> Items { get { return items; } set { if (value != items) { items = value; OnPropertyChanged("Items"); } } }

        public event PropertyChangedEventHandler PropertyChanged;
        protected virtual void OnPropertyChanged(string name)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }

        public ItemsWindow(List<Item> items)
        {
            InitializeComponent();
            Items = items;
            DataContext = this;
        }
    }
}
