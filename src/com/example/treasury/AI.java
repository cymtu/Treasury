package com.example.treasury;

import java.util.Random;

public class AI {
    private LevelAI ai;
    private Treasury treasury;
    
    public AI(Treasury treasury, LevelAI ai){
    	this.treasury = treasury;
    	this.ai = ai;
    }
    
    public void setLevelAI(LevelAI ai){
    	this.ai = ai;
    }
    
    public LevelAI getLevelAI(){
    	return ai;
    }
    
    public int Course(){
    	int result=0;
        int lines = treasury.GetSCurrent().Getlines();
        int columns = treasury.GetSCurrent().Getcolumns();
        int CloseCell=0;
        double Average=0;
        double MaxLevel,MinLevel;                                      // Мах и Min ценнсть всех закрытых ячеек на доске
        int min;
        double [][] Density = new double[lines][columns];
        double [][] CountDensity = new double[lines][columns];   
        double [][] tempDensity = new double[lines][columns];  
        int RestJewel=treasury.GetSCurrent().GetSumValueJewels();      // Всего драгоценностей на доске
        RestJewel=RestJewel - treasury.GetSumOpenJewels();             // Сумма всех драгоценностей, которые ещё не нашли
        
        // Находим сначала кол-во ещё закрытых ячеек
        for(int l=0;l<lines;l++)
           for(int c=0;c<columns;c++){   
               if(treasury.GetStatusCell(l, c)!=Treasury.TypeStatusCell.Open){
                  CloseCell++;
           }
        }
        
        // Находим среднее значение ценности каждой закрытой ячейки
        Average=1.0*RestJewel/CloseCell;
        MaxLevel=MinLevel=Average;   // Min=Max=Average
        // Раставляем начальное значение ценности каждой закрытой ячейки
         for(int l=0;l<lines;l++)
           for(int c=0;c<columns;c++){  
               CountDensity[l][c]=1;
               if(treasury.GetStatusCell(l, c)!=Treasury.TypeStatusCell.Open){
                  Density[l][c]=Average;
                  tempDensity[l][c]=Average;
               }
               else{
                   Density[l][c]=-1;  // Открыте ячейки
                   tempDensity[l][c]=-1;  // Открыте ячейки
            }
        }
         
         // Начинаем растановку на повышение
         for(int l=0;l<lines;l++)
           for(int c=0;c<columns;c++){   
               if(treasury.GetStatusCell(l, c)==Treasury.TypeStatusCell.Open && treasury.GetValueCell(l, c)>=0){
                  //Average=1.0*(ValueCell[l][c] - GetFind(l,c))/GetFindCloseCell(l,c);   // Средняя ценность закрытых ячеек для данной открытой ячеики. 
                   Average=1.0*(treasury.GetValueCell(l, c))/treasury.GetFindCloseCell(l,c);
                         //1
                         for(int i=0;i<columns;i++) if(treasury.GetStatusCell(l, i)!=Treasury.TypeStatusCell.Open && Density[l][i]>0){
                              tempDensity[l][i]+=Average;
                              CountDensity[l][i]++;
                              Density[l][i]=tempDensity[l][i]/CountDensity[l][i];
                              if(Average==0)Density[l][i]=0;
                          }
                         //2
                         for(int i=0;i<lines;i++) if(treasury.GetStatusCell(i, c)!=Treasury.TypeStatusCell.Open && Density[i][c]>0){
                             tempDensity[i][c]+=Average;
                             CountDensity[i][c]++;
                             Density[i][c]=tempDensity[i][c]/CountDensity[i][c];
                             if(Average==0)Density[i][c]=0;
                         }
                         //3
                         min=Math.min(l, c);
                         for(int i=0; (l- min + i)<lines && (c - min + i)<columns;i++) if(treasury.GetStatusCell(l - min + i, c - min + i)!=Treasury.TypeStatusCell.Open && Density[l - min + i][c - min + i]>0){
                             tempDensity[l - min + i][c - min + i]+=Average;
                             CountDensity[l - min + i][c - min + i]++;
                             Density[l - min + i][c - min + i]=tempDensity[l - min + i][c - min + i]/CountDensity[l - min + i][c - min + i];
                             if(Average==0)Density[l - min + i][c - min + i]=0;                           
                         }
                         //4
                         min=Math.min(lines-l-1, c);
                         for(int i=0; (l + min - i)>=0 && (c - min + i)<columns;i++) if(treasury.GetStatusCell(l + min - i, c - min + i)!=Treasury.TypeStatusCell.Open && Density[l + min - i][c - min + i]>0){
                             tempDensity[l + min - i][c - min + i]+=Average;
                             CountDensity[l + min - i][c - min + i]++;
                             Density[l + min - i][c - min + i]=tempDensity[l + min - i][c - min + i]/CountDensity[l + min - i][c - min + i];
                             if(Average==0)Density[l + min - i][c - min + i]=0;                     
                         }  
               }
           }        
       

         // Находим Min и Max уровни ячеек
         MinLevel=1000000;MaxLevel=0;
         for(int l=0;l<lines;l++)
          for(int c=0;c<columns;c++){   
                          if(Density[l][c]>=0){
                            if(Density[l][c]>MaxLevel)MaxLevel=Density[l][c];
                            if(Density[l][c]<MinLevel)MinLevel=Density[l][c];  
                          }
          }
        // Расчитываем уровни для фильтра всех подходящи ходов
        double tempMax,tempMin;         
        tempMax=MaxLevel - (LevelAI.Max - LevelAI.Max)*(MaxLevel-MinLevel)/(LevelAI.Max - LevelAI.Min);
        tempMin=MinLevel + (ai.getLevelAI() - LevelAI.Min)*(MaxLevel-MinLevel)/(LevelAI.Max - LevelAI.Min);
        MaxLevel=tempMax;
        MinLevel=tempMin;

        int maxl=0,maxc=0;
        int max=0;
        // Находим кол-во возможных ходов удовлетворяющих фильтру
        for(int l=0;l<lines;l++)
          for(int c=0;c<columns;c++){   
                if(Density[l][c]>=MinLevel && Density[l][c]<=MaxLevel){
                    max++;
                }
          }       
        
        // Находим ход из возможных вариантов
        Random rand = new Random();
        if(max==0)
        { // Если вариантов ходов вообще нет (такое возможно когда фильтр слишком узкий (MaxLevel-MinLevel)<<100)
          // То находим ближайщий уровень way к MaxLevel
          double way=10000;
          for(int l=0;l<lines;l++)
          for(int c=0;c<columns;c++)
          {   
                if(Math.abs(MaxLevel-Density[l][c])<way)
                {
                   way=Math.abs(MaxLevel-Density[l][c]);
                }
          } 
          // Находим кол-во ячеек удовлеворяющий уровню way
          for(int l=0;l<lines;l++)
          for(int c=0;c<columns;c++)
          {   
                if(Math.abs(MaxLevel-Density[l][c])==way)
                {
                    max++;
                }
          }          
          // Выбираем из найденных ячеек случайно ячейку для хода
          max = rand.nextInt(max)+1;
          for(int l=0;l<lines;l++)
          for(int c=0;c<columns;c++)
          {   
                if(Math.abs(MaxLevel-Density[l][c])==way)
                {
                    max--;
                    if(max==0)
                    {
                        maxl=l;maxc=c;
                    }
                }
          }           
        }
        else
        {  // Если max!=0, то ищщем сразу возможный ход
           max = rand.nextInt(max)+1;
        
          // Выбираем ход
          for(int l=0;l<lines;l++)
          for(int c=0;c<columns;c++)
          {   
                if(Density[l][c]>=MinLevel && Density[l][c]<=MaxLevel)
                {
                    max--;
                    if(max==0)
                    {
                        maxl=l;maxc=c;
                    }
                }
          }
        }
        
        // Компьютер делает ход
        result = treasury.Course(maxl, maxc);       
        
    	return result;
    }
}
