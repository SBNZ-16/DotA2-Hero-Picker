using DotaPickerFront.model;
using DotaPickerFront.ultility;
using MaterialDesignThemes.Wpf;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Drawing;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Web.Script.Serialization;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using Image = System.Windows.Controls.Image;

namespace DotaPickerFront
{
    /// <summary>
    /// Interaction logic for ItemsWindow.xaml
    /// </summary>
    public partial class ItemsWindow : Window, INotifyPropertyChanged
    {
        private ObservableCollection<Item> items;
        private ObservableCollection<Item> itemsSource;
        private int neededToComplete = 0;
        private Item emptyItem = new Item
        {
            Name = "Item not selected",
            Image = BitmapConverter.BitmapToImageSource(new Bitmap(System.Drawing.Image.FromFile("../../resources/items/empty_item.png")))
        };
        private int boughtItemCount = 0;
        public ObservableCollection<Item> ItemsToBuy { get; set; } = new ObservableCollection<Item>();
        public ObservableCollection<Item> BoughtItems { get; set; } = new ObservableCollection<Item>();


        public ObservableCollection<Item> Items { get { return items; } set { if (value != items) { items = value; OnPropertyChanged("Items"); } } }
        public int NeededToComplete { get { return neededToComplete; } set { if (value != neededToComplete) { neededToComplete = value; OnPropertyChanged("NeededToComplete"); } } }

        public event PropertyChangedEventHandler PropertyChanged;
        protected virtual void OnPropertyChanged(string name)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }


        public SnackbarMessageQueue MyCustomMessageQueue { get; set; }

        private static readonly HttpClient client = new HttpClient();

        public ItemsWindow(List<Item> items)
        {
            InitializeComponent();
            DataContext = this;
            MyCustomMessageQueue = new SnackbarMessageQueue(TimeSpan.FromMilliseconds(1000));
            itemsSource = new ObservableCollection<Item>(items);
            Items = new ObservableCollection<Item>(items);
            for (int i = 0; i < 9; i++)
                BoughtItems.Add(emptyItem);
        }

        private void ItemIcon_MouseLeave(object sender, MouseEventArgs e)
        {

            Image itemImage = (Image)sender;
            itemImage.Width = 55;
            itemImage.Height = 40;
            itemImage.Margin = new Thickness(5, 3, 5, 3);

        }

        private void ItemIcon_MouseEnter(object sender, MouseEventArgs e)
        {
            Image itemImage = (Image)sender;
            itemImage.Width = 65;
            itemImage.Height = 46;
            itemImage.Margin = new Thickness(0, 0, 0, 0);
        }

        private async void ItemIcon_RightMouseUp(object sender, MouseEventArgs e)
        {
            Image itemImage = (Image)sender;
            Item itemToBuy = (Item)itemImage.DataContext;
            ItemsToBuy.Clear();
            ItemsToBuy.Add(itemToBuy);


            var boughtItemsNames = new List<string>();
            for(int i = 0; i< boughtItemCount; i++)
            {
                boughtItemsNames.Add(BoughtItems[i].Name);
            }

            var postDict = new Dictionary<string, object>();
            postDict["wanted"] = itemToBuy.Name;
            postDict["boughtItems"] = boughtItemsNames;
           
            var content = new JavaScriptSerializer().Serialize(postDict);
            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Post, "http://localhost:8080/api/items"); //TODO endpoint promeni
            request.Content = new StringContent(content, Encoding.UTF8, "application/json");
            try
            {
                var response = await client.SendAsync(request);
                var responseString = await response.Content.ReadAsStringAsync();
                NeededToComplete = Int32.Parse(responseString);
            }
            catch (Exception)
            {
                MyCustomMessageQueue.Enqueue("Error in communication with a server");
            }
        }

        private void ItemIcon_LeftMouseUp(object sender, MouseEventArgs e)
        {
            Image itemImage = (Image)sender;
            if (boughtItemCount < 9)
            {
                BoughtItems[boughtItemCount] = (Item)itemImage.DataContext;
                boughtItemCount++;
            }
        }

        private void RemoveBoughtItem(object sender, MouseEventArgs e)
        {
            Image itemImage = (Image)sender;
            Item itemToRemove = (Item)itemImage.DataContext;
            if (itemToRemove != emptyItem)
            {
                BoughtItems.Remove(itemToRemove);
                BoughtItems.Add(emptyItem);
                boughtItemCount--;
            }
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

        private void TreeViewItem_Selected(object sender, RoutedEventArgs e)
        {
            TreeViewItem tvi = e.OriginalSource as TreeViewItem;

            if (tvi == null || e.Handled) return;

            tvi.IsExpanded = !tvi.IsExpanded;
            tvi.IsSelected = false;

            e.Handled = true;
        }

        private void TreeViewItem_PreviewMouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            //this will suppress the event that is causing the nodes to expand/contract 
            e.Handled = true;
        }
    }
}
