package com.example.treasury;

import java.util.Random;

public class Treasury{
   private SizeBoard SizeCurrent;
   // Тип статуса ячейки
   public enum TypeStatusCell{Open, Close, Metka};
   // Field of Game
   // if value<0 then jewel
   // if value>=0 then number 
   private int [][] ValueCell;
   // Status cell of Field
   // 0 - cell open
   // 1 - cell close
   // 2 - metka
   // 3 - metka2
   private TypeStatusCell [][] StatusCell;

   private int SumOpenJewels;
   // Course
   private int Course;
   // Max and Min открытых цифр
   private int MIN,MAX;
   // Constructor
   public Treasury(SizeBoard SCurrent){
     NewGame(SCurrent);
   }    
   // return value of Field[line][column]
   public int GetValueCell(int line, int column){
       return ValueCell[line][column];
   }
   public TypeStatusCell GetStatusCell(int line, int column){
       return StatusCell[line][column];
   }   
   // return SizeCurrent
   public SizeBoard GetSCurrent(){
       return SizeCurrent;
   }  
   // Ход игрока
   public int Course(int line, int column)
   {
	   int result=0;
       if(StatusCell[line][column]==TypeStatusCell.Open) return 0;
       Course++;
       StatusCell[line][column]=TypeStatusCell.Open;
       if(ValueCell[line][column]<0){
         SumOpenJewels+=Math.abs(ValueCell[line][column]);
         result = Math.abs(ValueCell[line][column]);
         MinusValueJewel(line,column);
       }else{
    	   if(ValueCell[line][column]==0) MarkAllEmpty(line, column);
       }
       
       CalMaxAndMin();
       
       return result;
   }
   
   private void CalMaxAndMin(){
	   MAX=-1000;
	   MIN=1000;
	   int lines = SizeCurrent.Getlines();
	   int columns = SizeCurrent.Getcolumns();
	      
	   for(int l=0;l<lines;l++)
	       for(int c=0;c<columns;c++){
	    	   if(ValueCell[l][c]>=0 && StatusCell[l][c]==TypeStatusCell.Open){
	    		   if(ValueCell[l][c]>MAX) MAX=ValueCell[l][c];
	    		   if(ValueCell[l][c]<MIN) MIN=ValueCell[l][c];	    		   
	    	   }
	       }	   
   }
   
   public int getMAX(){
	   return MAX;
   }
   
   public int getMIN(){
	   return MIN;
   }
   
   private void MinusValueJewel(int l, int c){
       int min=0;
       int lines = SizeCurrent.Getlines();
       int columns = SizeCurrent.Getcolumns();        
       //1
       for(int i=0;i<columns;i++) {
    	   if(ValueCell[l][i]>0){ValueCell[l][i] = ValueCell[l][i] + ValueCell[l][c];}
    	   if(ValueCell[l][i]==0 && StatusCell[l][i]==TypeStatusCell.Open){ MarkAllEmpty(l, i);}
       }
       //2
       for(int i=0;i<lines;i++) {
    	   if(ValueCell[i][c]>0){ValueCell[i][c] = ValueCell[i][c] + ValueCell[l][c];}
    	   if(ValueCell[i][c]==0  && StatusCell[i][c]==TypeStatusCell.Open){MarkAllEmpty(i, c);}
       }
       //3
       min=Math.min(l, c);
       for(int i=0; (l- min + i)<lines && (c - min + i)<columns;i++) {
    	   if(ValueCell[l - min + i][c - min + i]>0){ValueCell[l - min + i][c - min + i] = ValueCell[l - min + i][c - min + i]  + ValueCell[l][c];}
    	   if(ValueCell[l - min + i][c - min + i]==0  && StatusCell[l - min + i][c - min + i]==TypeStatusCell.Open){MarkAllEmpty(l - min + i, c - min + i);}	   
       }
       //4
       min=Math.min(lines-l-1, c);
       for(int i=0; (l + min - i)>=0 && (c - min + i)<columns;i++) {
    	   if(ValueCell[l + min - i][c - min + i]>0){ValueCell[l + min - i][c - min + i] = ValueCell[l + min - i][c - min + i]  + ValueCell[l][c];}
    	   if(ValueCell[l + min - i][c - min + i]==0   && StatusCell[l + min - i][c - min + i]==TypeStatusCell.Open){MarkAllEmpty(l + min - i, c - min + i);}   	   
       }
       
   }
   
   public int GetCourse(){
       return Course;
   }
   // New game
   final public void NewGame(){
      int lines = SizeCurrent.Getlines();
      int columns = SizeCurrent.Getcolumns();
      ValueCell = new int [lines][columns];
      StatusCell = new TypeStatusCell [lines][columns]; 
      SumOpenJewels=0;
      Course=0;
      
      for(int l=0;l<lines;l++)
          for(int c=0;c<columns;c++){
              StatusCell[l][c]=TypeStatusCell.Close; // cell close
              ValueCell[l][c]=0;
              //System.out.println("l=" +l + " c=" + c);
          }
          // Растоновка драгоценностей
          RandomCollocation(SizeCurrent.GetCountNuggets(),SizeBoard.Nugget);
          RandomCollocation(SizeCurrent.GetCountAmethysts(),SizeBoard.Amethyst);
          RandomCollocation(SizeCurrent.GetCountChrysolites(),SizeBoard.Chrysolite);
          RandomCollocation(SizeCurrent.GetCountPearls(),SizeBoard.Pearl);
          RandomCollocation(SizeCurrent.GetCountSapphires(),SizeBoard.Sapphire);          
          RandomCollocation(SizeCurrent.GetCountRubies(),SizeBoard.Ruby);  
          // Расчитываем значение ячеек
          Calculate();
   }
   // Новая игра с параметрами пользователя 
   public void NewGame(SizeBoard SCurrent){
       this.SizeCurrent=SCurrent;
       NewGame();
   }
   // Random растановка
   private void RandomCollocation(int count, int value){
       int line;
       int column;
       Random rand = new Random();
       while(count>0){
           line = rand.nextInt(SizeCurrent.Getlines());
           column = rand.nextInt(SizeCurrent.Getcolumns());
           if(ValueCell[line][column]==0){
              ValueCell[line][column]=-value;
              //System.out.println("count=" + count + " line=" + line + " column=" + column);
              count--;
           }
       }
   }
   // Расчет ячеек
   private void Calculate(){
       int lines = SizeCurrent.Getlines();
       int columns = SizeCurrent.Getcolumns();       
       int sum;
       int min=0;
       for(int l=0;l<lines;l++)
          for(int c=0;c<columns;c++){  
        	  sum=0;
              min=0;
              if(ValueCell[l][c]==0){
                  //1
                  for(int i=0;i<columns;i++) if(ValueCell[l][i]<0){sum+=ValueCell[l][i];}
                  //2
                  for(int i=0;i<lines;i++) if(ValueCell[i][c]<0){sum+=ValueCell[i][c];}
                  //3
                  min=Math.min(l, c);
                  for(int i=0; (l- min + i)<lines && (c - min + i)<columns;i++) if(ValueCell[l - min + i][c - min + i]<0){sum+=ValueCell[l - min + i][c - min + i];}
                  //4
                  min=Math.min(lines-l-1, c);
                  for(int i=0; (l + min - i)>=0 && (c - min + i)<columns;i++) if(ValueCell[l + min - i][c - min + i]<0){sum+=ValueCell[l + min - i][c - min + i];}
                  
                  //
                  ValueCell[l][c]=-sum;
              }
          }      
   }
   // Находим сумму всех драгоценностей открытых полей которые видны из ячейки Cell(l,c)
   public int GetFind(int l, int c){
       int sum=0;
       int min=0;
       int lines = SizeCurrent.Getlines();
       int columns = SizeCurrent.Getcolumns();        
       //1
       for(int i=0;i<columns;i++) if(ValueCell[l][i]<0 && StatusCell[l][i]==TypeStatusCell.Open){sum+=ValueCell[l][i];}
       //2
       for(int i=0;i<lines;i++) if(ValueCell[i][c]<0 && StatusCell[i][c]==TypeStatusCell.Open){sum+=ValueCell[i][c];}
       //3
       min=Math.min(l, c);
       for(int i=0; (l- min + i)<lines && (c - min + i)<columns;i++) if(ValueCell[l - min + i][c - min + i]<0  && StatusCell[l - min + i][c - min + i]==TypeStatusCell.Open){sum+=ValueCell[l - min + i][c - min + i];}
       //4
       min=Math.min(lines-l-1, c);
       for(int i=0; (l + min - i)>=0 && (c - min + i)<columns;i++) if(ValueCell[l + min - i][c - min + i]<0 && StatusCell[l + min - i][c - min + i]==TypeStatusCell.Open){sum+=ValueCell[l + min - i][c - min + i];}
       
       return -1*sum;
   }
   // Находим кол-во полей которые закрыты и видны из ячейки Cell(l,c)
   public int GetFindCloseCell(int l, int c){
       int min=0;
       int sum=0;
       int lines = SizeCurrent.Getlines();
       int columns = SizeCurrent.Getcolumns();        
       //1
       for(int i=0;i<columns;i++) if(StatusCell[l][i]!=TypeStatusCell.Open){sum++;}
       //2
       for(int i=0;i<lines;i++) if(StatusCell[i][c]!=TypeStatusCell.Open){sum++;}
       //3
       min=Math.min(l, c);
       for(int i=0; (l- min + i)<lines && (c - min + i)<columns;i++) if(StatusCell[l - min + i][c - min + i]!=TypeStatusCell.Open){sum++;}
       //4
       min=Math.min(lines-l-1, c);
       for(int i=0; (l + min - i)>=0 && (c - min + i)<columns;i++) if(StatusCell[l + min - i][c - min + i]!=TypeStatusCell.Open){sum++;}
       
       return sum;
   }   
   // Помечаем меткой все ячеки, которые видны из Cell(l,c)
   private void MarkAllEmpty(int l, int c){
      int lines = SizeCurrent.Getlines();
      int columns = SizeCurrent.Getcolumns(); 
      int min=0;
       for(int i=0;i<columns;i++) if(StatusCell[l][i]==TypeStatusCell.Close){StatusCell[l][i]=TypeStatusCell.Metka;}
       //2
       for(int i=0;i<lines;i++) if(StatusCell[i][c]==TypeStatusCell.Close){StatusCell[i][c]=TypeStatusCell.Metka;}
       //3
       min=Math.min(l, c);
       for(int i=0; (l- min + i)<lines && (c - min + i)<columns;i++) if(StatusCell[l - min + i][c - min + i]==TypeStatusCell.Close){StatusCell[l - min + i][c - min + i]=TypeStatusCell.Metka;}
       //4
       min=Math.min(lines-l-1, c);
       for(int i=0; (l + min - i)>=0 && (c - min + i)<columns;i++) if(StatusCell[l + min - i][c - min + i]==TypeStatusCell.Close){StatusCell[l + min - i][c - min + i]=TypeStatusCell.Metka;}         
   }
   // Проверка условия окончания игры
   public boolean EndGame(){
       int sum = SizeCurrent.GetSumValueJewels();
       if(sum==SumOpenJewels) return true;
       return false;
   }
   
   public int GetSumOpenJewels(){
	   return SumOpenJewels;
   }
}

