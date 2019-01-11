import java.io.*;
import java.util.Scanner;

public class Main{
	public static void main(String args[]){
		int rows, cols, min, max;
		try{
			Scanner inFile = new Scanner(new FileReader(args[0]));
			BufferedWriter outFile1 = new BufferedWriter(new FileWriter(args[1]));
			BufferedWriter outFile2 = new BufferedWriter(new FileWriter(args[2]));
			BufferedWriter outFile3 = new BufferedWriter(new FileWriter(args[3]));
			rows = inFile.nextInt();
			cols = inFile.nextInt();
			min = inFile.nextInt();
			max = inFile.nextInt();
			ImageProcessing image = new ImageProcessing(rows, cols, min, max);
			image.loadImage(inFile);
		
			image.firstPass_EuclidianDistance();
			outFile3.write("Pass-1 Result");
			outFile3.newLine();
			image.prettyPrintDistance(outFile3);
			
			image.secondPass_EuclidianDistance();
			image.printMinMax(outFile1);
			image.printImage(outFile1);
			outFile3.write("Pass-2 Result");
			outFile3.newLine();
			image.prettyPrintDistance(outFile3);
			
			image.compute_localMaxima();	
			image.printMinMax(outFile2);	
			image.printSkeleton(outFile2);
			outFile3.write("Skeleton Result");
			outFile3.newLine();
			image.prettyPrintSkeleton(outFile3);
			
			inFile.close();
			outFile1.close();
			outFile2.close();
			outFile3.close();
		}
	
		catch(Exception e){
			System.out.println(e.getMessage());
		} 
	}
}