package com.example.treasury;

import java.util.Random;

public class SizeBoard{
   // Тип игры по размеру	
   static public enum TypeJewel{Nugget, Amethyst, Chrysolite, Pearl, Sapphire, Ruby};	
   public enum TypeSize{Beginner, Beginner2, Beginner3, Expert,  Expert2,  Expert3, Professional,  Professional2,  Professional3, Special};
   private TypeSize _TypeSize;
   // Ценность каждой драгоценности(jewel) 
   public static final int Nugget = 1;
   public static final int Amethyst = 2;
   public static final int Chrysolite = 4;
   public static final int Pearl = 8;
   public static final int Sapphire = 16;
   public static final int Ruby = 32; 
   // Постоянные параметры игры
   public static final int Max=999;                // Общая сумма всех jewels <= 999
   public static final double MaxPrc=0.40;         // Кол-во всех jewels <= Round(MaxPrc*lines*columns)
   public static final int MinLines = 9;           // Минимальное кол-во линий
   public static final int MaxLines = 36;          // Максимальное кол-во линий
   public static final int MinColumns = 9;         // Минимальное кол-во столбцов
   public static final int MaxColumns = 36;        // Максимальное кол-во столбцов
   // Размеры доски
   private int lines;
   private int columns;
   // Кол-во каждой  jewels
   private int CountNuggets;
   private int CountAmethysts;
   private int CountChrysolites;
   private int CountPearls;
   private int CountSapphires;
   private int CountRubies;  
  
   SizeBoard(TypeSize typesize){        
         this._TypeSize = typesize;
         if(typesize == TypeSize.Beginner) Init(9, 9, 8, 4, 2, 1, 0, 0);  
         if(typesize == TypeSize.Beginner2) Init(9, 18, 16, 8, 4, 2, 0, 0);  
         if(typesize == TypeSize.Beginner3) Init(14, 18, 24, 12, 6, 3, 0, 0);        
         if(typesize == TypeSize.Expert) Init(13, 13, 16, 8, 4, 2, 1, 0); 
         if(typesize == TypeSize.Expert2) Init(13, 26, 32, 16, 8, 4, 2, 0); 
         if(typesize == TypeSize.Expert3) Init(20, 26, 48, 24, 12, 8, 4, 0);         
         if(typesize == TypeSize.Professional) Init(18, 18, 32, 16, 8, 4, 2, 1);     
         if(typesize == TypeSize.Professional2) Init(18, 36, 64, 32, 16, 8, 4, 2);  
         if(typesize == TypeSize.Professional3) Init(27, 36, 96, 48, 24, 12, 6, 3);          
         if(typesize == TypeSize.Special) Rand();         
   }
   
   SizeBoard(int lines, int columns,int CountNuggets,int CountAmethysts,int CountChrysolites,int CountPearls,int CountSapphires,int CountRubies){
	  Init(lines, columns, CountNuggets, CountAmethysts, CountChrysolites, CountPearls, CountSapphires, CountRubies);       
      // Правило №1 проверяет чтоб Кол-во всех jewels <= Round(MaxPrc*lines*columns)
      if(!RuleOne()){
        this.Rand();             
      }
      // Правило №2 проверяет чтоб Общая сумма всех jewels <= 999
      if(!RuleTwo()){              
        this.Rand();            
      }
      this._TypeSize =TypeSize.Special;
      if(this.equally(new SizeBoard(TypeSize.Beginner))) this._TypeSize = TypeSize.Beginner; 
      if(this.equally(new SizeBoard(TypeSize.Expert))) this._TypeSize = TypeSize.Expert;
      if(this.equally(new SizeBoard(TypeSize.Professional))) this._TypeSize = TypeSize.Professional;           
   }
   
   static public int Max(TypeJewel jewel, int lines, int columns,int CountNuggets,int CountAmethysts,int CountChrysolites,int CountPearls,int CountSapphires,int CountRubies){
	   int result = 0;
	   int sumJewels = Nugget*CountNuggets + Amethyst*CountAmethysts + Chrysolite*CountChrysolites + Pearl*CountPearls + Sapphire*CountSapphires + Ruby*CountRubies;
	   int count=CountNuggets + CountAmethysts + CountChrysolites + CountPearls + CountSapphires + CountRubies;
	   int maxCount = (int)(MaxPrc*lines*columns) - count;
	   int maxsumJewels = Max - sumJewels;
	   
	   if(jewel==TypeJewel.Nugget){
		   maxCount += CountNuggets;
		   maxsumJewels += Nugget*CountNuggets;
		   result = Math.min(maxCount, (int)(maxsumJewels/Nugget));
	   }
	   
	   if(jewel==TypeJewel.Amethyst){
		   maxCount += CountAmethysts;
		   maxsumJewels += Amethyst*CountAmethysts;
		   result = Math.min(maxCount, (int)(maxsumJewels/Amethyst));
	   }	 
	   
	   if(jewel==TypeJewel.Chrysolite){
		   maxCount += CountChrysolites;
		   maxsumJewels += Chrysolite*CountChrysolites;
		   result = Math.min(maxCount, (int)(maxsumJewels/Chrysolite));
	   }	
	   
	   if(jewel==TypeJewel.Pearl){
		   maxCount += CountPearls;
		   maxsumJewels += Pearl*CountPearls;
		   result = Math.min(maxCount, (int)(maxsumJewels/Pearl));
	   }	 
	   
	   if(jewel==TypeJewel.Sapphire){
		   maxCount += CountSapphires;
		   maxsumJewels += Sapphire*CountSapphires;
		   result = Math.min(maxCount, (int)(maxsumJewels/Sapphire));
	   }	
	   
	   if(jewel==TypeJewel.Ruby){
		   maxCount += CountRubies;
		   maxsumJewels += Ruby*CountRubies;
		   result = Math.min(maxCount, (int)(maxsumJewels/Ruby));
	   }   
	   
	   return result;
   }
   
   private boolean equally(SizeBoard size){
	   if(this.lines != size.lines) return false;
	   if(this.columns != size.columns) return false;
	   if(this.CountNuggets != size.CountNuggets) return false;
	   if(this.CountAmethysts != size.CountAmethysts) return false;
	   if(this.CountChrysolites != size.CountChrysolites) return false;	   
	   if(this.CountPearls != size.CountPearls) return false;
	   if(this.CountSapphires != size.CountSapphires) return false;
	   if(this.CountRubies != size.CountRubies) return false;	   	   
	   return true;
   }
   
   private void Init(int lines, int columns,int CountNuggets,int CountAmethysts,int CountChrysolites,int CountPearls,int CountSapphires,int CountRubies){
	      this.lines=lines;
	      this.columns=columns;
	      this.CountNuggets=CountNuggets;
	      this.CountAmethysts=CountAmethysts;
	      this.CountChrysolites=CountChrysolites;
	      this.CountPearls=CountPearls;
	      this.CountSapphires=CountSapphires;
	      this.CountRubies=CountRubies;  
	      // Проверка
	      if(this.lines<MinLines) this.lines=MinLines;
	      if(this.lines>MaxLines) this.lines=MaxLines;
	      if(this.columns<MinColumns) this.lines=MinColumns;
	      if(this.columns>MaxColumns) this.lines=MaxColumns; 
   }
   
   // Правило №1 true - все в порядке, false - правило не выполняеться
   private boolean RuleOne(){
      int sum=0;
      sum=CountNuggets + CountAmethysts + CountChrysolites + CountPearls + CountSapphires + CountRubies;
      if(sum>MaxPrc*lines*columns)
      {
        return false;  
        
      }          
      return true;
   }      
   // Правило №2 true - все в порядке, false - правило не выполняеться
   private boolean RuleTwo(){
      int sum=0;
      sum=Nugget*CountNuggets + Amethyst*CountAmethysts + Chrysolite*CountChrysolites + Pearl*CountPearls + Sapphire*CountSapphires + Ruby*CountRubies;
      if(sum>Max)
      {
        return false;           
      }          
      return true;
   }
   
   // Случано выбираем размеры доски и равномерно распределяем jewels
   // Кол-во jewels Должно быть равно Math.round(MaxPrc*this.lines*this.columns)=n;
   // CountNuggets + CountAmethysts + CountChrysolites + CountPearls + CountSapphires + CountRubies = n (1)
   // Причем сумма каждого вида jewel должны быть равны между собой для равномерного распределения, то есть
   // CountNuggets*Nugget = CountAmethysts*Amethyst = CountChrysolites*Chrysolite = CountPearls*Pearl = CountSapphires*Sapphire = CountRubies*Ruby (2)
   // Тогда все неизвестный с префиксом Count... можно выразить через неизвестную CountNuggets и тогда уравнение (1) примет следующий вид
   // CountNuggets*(1 + Nugget/Amethyst + Nugget/Chrysolite + Nugget/Pearl + Nugget/Sapphire + Nugget/Ruby) = n (1')
   // Из этого уравнения находим CountNuggets, а остальные из соотношения (2)
   // Для соблюдения правила №2 ограничиваем CountNuggets <= 160.
   private void Rand() {
     int count; 
     double Prc; 
     Random rand = new Random();
     this.lines = MinLines + rand.nextInt(MaxLines - MinLines + 1);
     this.columns = MinColumns + rand.nextInt(MaxColumns - MinColumns + 1); 
     count=(int) Math.round(MaxPrc*this.lines*this.columns);
     Prc=1. + 1.*Nugget/Amethyst + 1.*Nugget/Chrysolite + 1.*Nugget/Pearl + 1.*Nugget/Sapphire + 1.*Nugget/Ruby;
     this.CountNuggets=(int)Math.round(count/Prc);
     if(this.CountNuggets>160) this.CountNuggets=160;
     Prc=0.5;
     this.CountAmethysts=(int) Math.round(Prc*this.CountNuggets);
     this.CountChrysolites=(int) Math.round(Prc*this.CountAmethysts);
     this.CountPearls=(int) Math.round(Prc*this.CountChrysolites); 
     this.CountSapphires=(int) Math.round(Prc*this.CountPearls);
     this.CountRubies=(int) Math.round(Prc*this.CountSapphires);          
   }
              
   public int Getlines(){return lines;}
   public int Getcolumns(){return columns;}
   public int GetCountNuggets(){return CountNuggets;}  
   public int GetCountAmethysts(){return CountAmethysts;}
   public int GetCountChrysolites(){return CountChrysolites;}
   public int GetCountPearls(){return CountPearls;} 
   public int GetCountSapphires(){return CountSapphires;}
   public int GetCountRubies(){return CountRubies;} 
   public TypeSize getTypeSize(){return this._TypeSize;}
   
   public void setTypeSize(TypeSize typesize){
       this._TypeSize = typesize;
       if(typesize == TypeSize.Beginner) Init(9, 9, 8, 4, 2, 1, 0, 0);  
       if(typesize == TypeSize.Beginner2) Init(9, 18, 16, 8, 4, 2, 0, 0);  
       if(typesize == TypeSize.Beginner3) Init(14, 18, 24, 12, 6, 3, 0, 0);        
       if(typesize == TypeSize.Expert) Init(13, 13, 16, 8, 4, 2, 1, 0); 
       if(typesize == TypeSize.Expert2) Init(13, 26, 32, 16, 8, 4, 2, 0); 
       if(typesize == TypeSize.Expert3) Init(20, 26, 48, 24, 12, 8, 4, 0);         
       if(typesize == TypeSize.Professional) Init(18, 18, 32, 16, 8, 4, 2, 1);     
       if(typesize == TypeSize.Professional2) Init(18, 36, 64, 32, 16, 8, 4, 2);  
       if(typesize == TypeSize.Professional3) Init(27, 36, 96, 48, 24, 12, 6, 3);          
       if(typesize == TypeSize.Special) Rand();   	   
   }
   
   public int GetSumValueJewels(){
       return Nugget*CountNuggets + Amethyst*CountAmethysts + Chrysolite*CountChrysolites + Pearl*CountPearls + Sapphire*CountSapphires + Ruby*CountRubies;
   }
}