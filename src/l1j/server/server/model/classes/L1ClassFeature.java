 package l1j.server.server.model.classes;


 public abstract class L1ClassFeature
 {
   public static L1ClassFeature newClassFeature(int classId) {
     if (classId == 0 || classId == 1)
     {
       return new L1RoyalClassFeature();
     }
     if (classId == 138 || classId == 37)
     {
       return new L1ElfClassFeature();
     }
     if (classId == 20553 || classId == 48)
     {
       return new L1KnightClassFeature();
     }
     if (classId == 20278 || classId == 20279)
     {
       return new L1WizardClassFeature();
     }
     if (classId == 2786 || classId == 2796)
     {
       return new L1DarkElfClassFeature();
     }
     if (classId == 6658 || classId == 6661)
     {
       return new L1DragonKnightClassFeature();
     }
     if (classId == 6671 || classId == 6650)
     {
       return new L1BlackWizardClassFeature();
     }
     if (classId == 20567 || classId == 20577)
     {
       return new L1戰士ClassFeature();
     }
     if (classId == 18520 || classId == 18499)
     {
       return new L1FencerClassFeature();
     }
     if (classId == 19296 || classId == 19299)
     {
       return new L1LancerClassFeature();
     }
     throw new IllegalArgumentException();
   }

   public abstract int getMagicLevel(int paramInt);

   public abstract String getClassInitial();
 }


