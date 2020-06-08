using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DotaPickerFront.model
{
    public class RoleStats : INotifyPropertyChanged
    {
        private double heroesPerRole;
        private double scoreLossPercentage;
        public double HeroesPerRole { get { return heroesPerRole; } set { if (value != heroesPerRole) { heroesPerRole = value; OnPropertyChanged("HeroesPerRole"); } } }
        public double ScoreLossPercentage { get { return scoreLossPercentage; } set { if (value != scoreLossPercentage) { scoreLossPercentage = value; OnPropertyChanged("ScoreLossPercentage"); } } }

        public event PropertyChangedEventHandler PropertyChanged;
        protected virtual void OnPropertyChanged(string name)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }
    }
}
