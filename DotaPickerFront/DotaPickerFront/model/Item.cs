using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;

namespace DotaPickerFront.model
{
    public class Item
    {
        public string Name { get; set; }

        public int Cost { get; set; }

        public BitmapImage Image { get; set; }

        public List<string> Components { get; set; }
    }
}
