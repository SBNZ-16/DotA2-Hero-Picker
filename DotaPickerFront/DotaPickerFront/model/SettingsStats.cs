using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DotaPickerFront.model
{
    public class SettingsStats : INotifyPropertyChanged
    {
        private String rulesTemplate;
        private String vanillaRulesTempalte;
        private double enemyHeroDisadvantage;
        private double enemyLaneHeroDisadvantage;
        private double preferredHeroFactor;
        private double preferredLaneFactor;
        private double preferredRoleFactor;

        public String RulesTemplate { get { return rulesTemplate; } set { if (value != rulesTemplate) { rulesTemplate = value; OnPropertyChanged("RulesTemplate"); } } }
        public String VanillaRulesTempalte { get { return vanillaRulesTempalte; } set { if (value != vanillaRulesTempalte) { vanillaRulesTempalte = value; OnPropertyChanged("VanillaRulesTempalte"); } } }
        public double EnemyHeroDisadvantage { get { return enemyHeroDisadvantage; } set { if (value != enemyHeroDisadvantage) { enemyHeroDisadvantage = value; OnPropertyChanged("VnemyHeroDisadvantage"); } } }
        public double EnemyLaneHeroDisadvantage { get { return enemyLaneHeroDisadvantage; } set { if (value != enemyLaneHeroDisadvantage) { enemyLaneHeroDisadvantage = value; OnPropertyChanged("EnemyLaneHeroDisadvantage"); } } }
        public double PreferredHeroFactor { get { return preferredHeroFactor; } set { if (value != preferredHeroFactor) { preferredHeroFactor = value; OnPropertyChanged("PreferredHeroFactor"); } } }
        public double PreferredLaneFactor { get { return preferredLaneFactor; } set { if (value != preferredLaneFactor) { preferredLaneFactor = value; OnPropertyChanged("PreferredLaneFactor"); } } }
        public double PreferredRoleFactor { get { return preferredRoleFactor; } set { if (value != preferredRoleFactor) { preferredRoleFactor = value; OnPropertyChanged("PreferredRoleFactor"); } } }
        public RoleStats Nuker { get; set; }
        public RoleStats Escape { get; set; }
        public RoleStats Support { get; set; }
        public RoleStats Initiator { get; set; }
        public RoleStats Carry { get; set; }
        public RoleStats Jungler { get; set; }
        public RoleStats Durable { get; set; }
        public RoleStats Disabler { get; set; }
        public RoleStats Pusher { get; set; }

        public event PropertyChangedEventHandler PropertyChanged;
        protected virtual void OnPropertyChanged(string name)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }
    }
}
