import java.io.*;
import java.util.Scanner;

public class ImageProcessing{
	private int numRows;
	private int numCols;
	private double minVal;
	private double maxVal;
	private double newMinVal;
	private double newMaxVal;
	private double zeroFramedAry[][];
	private int skeletonAry[][];
	private double neighborAry[];
	
	public ImageProcessing(int rows, int cols, int min, int max){
		numRows = rows;
		numCols = cols;
		minVal = min * 1.0;
		maxVal = max * 1.0;
		zeroFramedAry = new double[numRows+2][numCols+2];
		skeletonAry = new int[numRows+2][numCols+2];
		neighborAry = new double[8];
	}
	
	public void loadImage(Scanner inFile){
		for(int i = 1; i <= numRows; i++){
			for(int j = 1; j <= numCols; j++){
				zeroFramedAry[i][j] = inFile.nextInt();
			}
		}
	}
	
	public void loadNeighbors(int pass_number, int i, int j){
		if(pass_number == 1){
			neighborAry[0] = (zeroFramedAry[i-1][j-1]) + Math.sqrt(2);
			neighborAry[1] = (zeroFramedAry[i-1][j]) + 1;
			neighborAry[2] = (zeroFramedAry[i-1][j+1]) + Math.sqrt(2);
			neighborAry[3] = (zeroFramedAry[i][j-1]) + 1;
		}
		
		else if(pass_number == 2){
			neighborAry[4] = (zeroFramedAry[i][j+1]) + 1;
			neighborAry[5] = (zeroFramedAry[i+1][j-1]) + Math.sqrt(2);
			neighborAry[6] = (zeroFramedAry[i+1][j]) + 1;
			neighborAry[7] = (zeroFramedAry[i+1][j+1]) + Math.sqrt(2);		
		}
		
		else{
			neighborAry[0] = zeroFramedAry[i-1][j-1];
			neighborAry[1] = zeroFramedAry[i-1][j];
			neighborAry[2] = zeroFramedAry[i-1][j+1];
			neighborAry[3] = zeroFramedAry[i][j-1];
			neighborAry[4] = zeroFramedAry[i][j+1];
			neighborAry[5] = zeroFramedAry[i+1][j-1];
			neighborAry[6] = zeroFramedAry[i+1][j];
			neighborAry[7] = zeroFramedAry[i+1][j+1];
		}
	}
	
	public void firstPass_EuclidianDistance(){
		int neighbor;
		double min;
		
		for(int i = 1; i <= numRows; i++){
			for(int j = 1; j <= numCols; j++){
				if(zeroFramedAry[i][j] > 0){	
					loadNeighbors(1, i, j);
					min = neighborAry[0];						
					neighbor = 1;
					while(neighbor < 4){
						if(neighborAry[neighbor] < min)
							min = neighborAry[neighbor];
						neighbor++;
					}
					zeroFramedAry[i][j] = min;
				}
			}
		}
	}
	
	public void secondPass_EuclidianDistance(){
 		int neighbor;
		double min;
		newMinVal = numRows*numCols;	
		newMaxVal = 0;
		
		for(int i = numRows; i >= 1; i--){
			for(int j = numCols; j >= 1; j--){
				if(zeroFramedAry[i][j] > 0){
					loadNeighbors(2, i, j);
					min = zeroFramedAry[i][j];
					neighbor = 4;
					while(neighbor < 8){
						if(neighborAry[neighbor] < min)
							min = neighborAry[neighbor];
						neighbor++;
					}
					zeroFramedAry[i][j] = min;
				}					
				if(zeroFramedAry[i][j] > newMaxVal)
					newMaxVal = zeroFramedAry[i][j];
				if(zeroFramedAry[i][j] < newMinVal)
					newMinVal = zeroFramedAry[i][j];
			}
		} 
	}
	
	public Boolean is_maxima(int i, int j){
		loadNeighbors(3, i, j);
		int neighbor = 0;	
		while(neighbor < 8){
			if(zeroFramedAry[i][j] < neighborAry[neighbor])
				return false;
			neighbor++;
		}
		return true;
	}
	
	public void compute_localMaxima(){
		for(int i = 1; i <= numRows; i++){
			for(int j = 1; j <= numCols; j++){
				if((zeroFramedAry[i][j] > 0) && is_maxima(i, j))
					skeletonAry[i][j] = 1;
				else skeletonAry[i][j] = 0;
			}
		}
	}
	
	public void printImage(BufferedWriter outFile1){
		try{
			for(int i = 1; i <= numRows; i++){
				for(int j = 1; j <= numCols; j++){
					outFile1.write(String.format("%.02f", zeroFramedAry[i][j]) + " ");
				}
				outFile1.newLine();
			}
		}
		
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void printSkeleton(BufferedWriter outFile2){
		try{
			for(int i = 1; i <= numRows; i++){
				for(int j = 1; j <= numCols; j++){
					outFile2.write(skeletonAry[i][j] + " ");
				}
				outFile2.newLine();
			}
		}
		
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void prettyPrintDistance(BufferedWriter outFile3){
		try{			
			 for(int i = 0; i <= numRows+1; i++){
				for(int j = 0; j <= numCols+1; j++){
					if(zeroFramedAry[i][j] > 0){
						int d = (int)zeroFramedAry[i][j];
						outFile3.write(d + "  ");
					}
					else if((j <= numCols && zeroFramedAry[i][j+1] > 0) || (j > 0 && zeroFramedAry[i][j-1] > 0))
						outFile3.write("  ");
					else outFile3.write("    ");
				}
				outFile3.newLine();
			}
			outFile3.newLine(); 
		}
		
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void prettyPrintSkeleton(BufferedWriter outFile3){
		try{			
			for(int i = 0; i <= numRows+1; i++){
				for(int j = 0; j <= numCols+1; j++){
					if(skeletonAry[i][j] > 0)
						outFile3.write("9  ");
					else 
						outFile3.write(".   ");
				}
				outFile3.newLine();
			}
			outFile3.newLine();
		} 
		
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void printMinMax(BufferedWriter outFile){
		try{
			outFile.write(String.format("%.02f", newMinVal) + " " + String.format("%.02f", newMaxVal));
			outFile.newLine();
		}
		
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}