package com.example.treasury;

import java.util.Random;

public class LevelAI {
	
	   public enum TypeAI{NoAI, Beginner, Expert, Professional, Special};     // Уровень игры компьютера
	   public static final int Max=100;                                       // Максимальный верхний уровень
	   public static final int Min=0;                                         // Минимальный нижний уровень
	   
	   private TypeAI _TypeAI;
	   private int LevelAI;  

	   LevelAI(int LevelAI){
	      this.LevelAI=LevelAI;
	      if(this.LevelAI<Min) this.LevelAI=Min;
	      if(this.LevelAI>Max) this.LevelAI=Max; 
	      switch (LevelAI){
	         case 30: _TypeAI = TypeAI.Beginner;
	                  break;
	         case 60: _TypeAI = TypeAI.Expert;
                      break;	
	         case 90: _TypeAI = TypeAI.Professional;
                      break;  
             default: _TypeAI = TypeAI.Special;
                      break;
	      }
	   }
	   
	   LevelAI(TypeAI ai){
           setLevelAI(ai);
	   }
	   
	   public int getLevelAI(){
		   return LevelAI;
	   } 
	   
	   public TypeAI getTypeAI(){
		   return _TypeAI;
	   }
	   
	   public void setLevelAI(TypeAI ai){
			  Random rand = new Random();
			  _TypeAI = ai;
			  if(ai == TypeAI.NoAI)             LevelAI = 0;
			  if(ai == TypeAI.Beginner)         LevelAI = 30;
			  if(ai == TypeAI.Expert)           LevelAI = 60;
			  if(ai == TypeAI.Professional)     LevelAI = 90;	
			  if(ai == TypeAI.Special)          LevelAI = Min + rand.nextInt(Max - Min + 1);		   
	   }
}
