package network;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;

import math.Matrix;
import math.MatrixMath;
import math.Sigmoid;

public class Network {
	
	public int inputSize, hiddenSize, outputSize;
	
	public Matrix inputWeights, inputBiases, hiddenWeights, hiddenBiases;
	
	public Network(Network clone){
		this.inputSize = clone.inputSize;
		this.hiddenSize = clone.hiddenSize;
		this.outputSize = clone.outputSize;
		
		this.inputWeights = clone.inputWeights;
		this.inputBiases = clone.inputBiases;
		this.hiddenBiases = clone.hiddenBiases;
		this.hiddenWeights = clone.hiddenWeights;
	}
	
	public Network(double size){
		
		inputSize = 18;
		hiddenSize = 9;
		outputSize = 9;
		
		inputWeights = new Matrix(inputSize,hiddenSize);
		inputBiases = new Matrix(1, hiddenSize);
		
		hiddenWeights = new Matrix(hiddenSize,outputSize);
		hiddenBiases = new Matrix(1, outputSize);
		
		inputWeights.randomize(-size, size);
		inputBiases.randomize(-size, size);
		
		hiddenWeights.randomize(-size, size);
		hiddenBiases.randomize(-size, size);
		
	}
	
	public Network(String s){
		
		String[] chars = s.split(" ");
		
		inputSize = Integer.parseInt(chars[0]);
		hiddenSize = Integer.parseInt(chars[1]);
		outputSize = Integer.parseInt(chars[2]);
		
		int i = 3;
		
		double[][] inputWeightA = new double[inputSize][hiddenSize], inputBiasA = new double[1][hiddenSize], 
				   hiddenWeightA = new double[hiddenSize][outputSize], hiddenBiasA = new double[1][outputSize];
		
		int temp = i;
		
		for(;i < temp + inputSize * hiddenSize; i++){
			
			int x = (i - temp)/inputSize, y = (i-temp)%inputSize;
			
			inputWeightA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		temp = i;
		
		for( ;i < temp + outputSize * hiddenSize; i++){
			
			int x = (i - temp)/hiddenSize, y = (i-temp)%hiddenSize;
			
			hiddenWeightA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		temp = i;
		
		for( ;i < temp + hiddenSize; i++){
			
			int x = i - temp, y = 0;
			
			inputBiasA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		temp = i;
		
		for( ;i < temp + outputSize; i++){
			
			int x = i - temp, y = 0;
			
			hiddenBiasA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		inputWeights = new Matrix(inputWeightA);
		inputBiases = new Matrix(inputBiasA);
		
		hiddenWeights = new Matrix(hiddenWeightA);
		hiddenBiases = new Matrix(hiddenBiasA);
		
	}
	
	public Network(Network male, Network female){
		
		inputSize = male.inputSize;
		hiddenSize = male.hiddenSize;
		outputSize = male.outputSize;
		
		byte[] a = Network.toByteArray(male.toDoubleArry()), b = Network.toByteArray(female.toDoubleArry()), net = new byte[a.length];
		
		ArrayList<Integer> splices = new ArrayList<Integer>();
		
		for(int i = 0; i < Math.random()*64+32; i++){
			splices.add((int)Math.random()*a.length*8);
		}
		
		splices.add(a.length*8);
		
		Collections.sort(splices);
		
		int offset = (int)Math.random()*2;
		
		int i = 0;
		
		for(int j = 0; j < splices.size(); j++){
			
			for(int k = i*8; k < splices.get(j); k+=8){
				
				if(splices.get(j) - k  > 8){
					
					if(j + offset % 2 == 0){
						net[i] = a[i];
					}else net[i] = b[i];
				}else{
					int p = 9-splices.get(j) - k;
					if(j + offset % 2 == 0){
						net[i] += (a[i]>>p)<<p;
						net[i] += ((b[i]<<(8-p)) & 255)>>(8-p);
					}else{
						net[i] += (b[i]>>p)<<p;
						net[i] += ((a[i]<<(8-p)) & 255)>>(8-p);
					}
				}
				i++;
			}
			
		}
		
		buildNet(Network.toDouble(net));
		
		//System.out.println(hiddenWeights.getAttribute(1, 2));
		
	}
	
	public Network mutate(){
		
		Network output = (Network)this;
		
		output.inputSize = inputSize;
		output.hiddenSize = hiddenSize;
		output.outputSize = outputSize;
		
		byte[] data = Network.toByteArray(output.toDoubleArry().clone());
		
		for(int i = 0; i < (int)(Math.random()*3)+1; i++){
			
			int pos = (int)(Math.random()*(data.length*8*57/64));
			
			int dP = pos/57;
			
			pos += dP*7;
			
			if(pos%64 > 0)pos+=7;
			
			int p = pos%8;
			
			data[pos/8] = (byte) ((((data[pos/8] >> (8-p))&255) << (8-p)) + (((~(data[pos/8]>>(7-p))) & 1) << (7-p)) + (((data[pos/8]<<(p+1)) & 255) >> (p+1)));
			
			
		}
		
		//System.out.println(Network.toDouble(data)[5]);
		double [] out = Network.toDouble(data);
		
//		for(double d: out){
//			if(Double.isNaN(d))d = 0;
//		}
		
		output.buildNet(out);
		
		return output;
		
	}
	
	private void buildNet(double[] net){
		
		double[][] inputWeightA = new double[inputSize][hiddenSize], inputBiasA = new double[1][hiddenSize], 
				   hiddenWeightA = new double[hiddenSize][outputSize], hiddenBiasA = new double[1][outputSize];
		
		int i = 0;
		
		int temp = i;
		
		for(;i < temp + inputSize * hiddenSize; i++){
			
			int x = (i - temp)/inputSize, y = (i-temp)%inputSize;
			
			inputWeightA[y][x] = net[i];
			
		}
		
		temp = i;
		
		for( ;i < temp + outputSize * hiddenSize; i++){
			
			int x = (i - temp)/hiddenSize, y = (i-temp)%hiddenSize;

			hiddenWeightA[y][x] = net[i];
			
		}
		
		temp = i;
		
		for( ;i < temp + hiddenSize; i++){
			
			int x = i - temp, y = 0;
			
			inputBiasA[y][x] = net[i];
			
		}
		
		temp = i;
		
		for( ;i < temp + outputSize; i++){
			
			int x = i - temp, y = 0;
			
			hiddenBiasA[y][x] = net[i];
			
		}
		
		inputWeights = new Matrix(inputWeightA);
		inputBiases = new Matrix(inputBiasA);
		
		hiddenWeights = new Matrix(hiddenWeightA);
		hiddenBiases = new Matrix(hiddenBiasA);
		
	}

	
	public Matrix test(Matrix inputs){
		
		//Matrix delta1 = MatrixMath.add(MatrixMath.multiply(inputs, inputWeights), inputBiases);
		Matrix delta1 = MatrixMath.multiply(inputs, inputWeights);
		
		//Matrix delta2 = MatrixMath.add(MatrixMath.multiply(Sigmoid.sigmoid(delta1), hiddenWeights), hiddenBiases);
		Matrix delta2 = MatrixMath.multiply(Sigmoid.sigmoid(delta1), hiddenWeights);
		
		return Sigmoid.sigmoid(delta2);
		
	}
	
	private double sigs(double input){
		
		int pow = (int)Math.log10(input);
		
		input = (input/Math.pow(10, pow))*1000;
		
		input = (int)input;
		
		input = (input/1000)*Math.pow(10, pow);
		
		return input;
		
	}
	
	public String toString(){
		
		String output = inputSize + " " + hiddenSize + " " + outputSize + " ";
		
		
		for(double d:inputWeights.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		for(double d:hiddenWeights.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		for(double d:inputBiases.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		for(double d:hiddenBiases.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		return output;
		
	}
	
	public double[] toDoubleArry(){
		
		double[] output = new double[(inputSize + outputSize + 1)*(hiddenSize) + outputSize];
		
		int i = 0;
		
		for(double d:inputWeights.getSingleArray()){
			output[i] = d;
			i++;
		}
		
		for(double d:hiddenWeights.getSingleArray()){
			output[i] = d;
			i++;		
		}
		
		for(double d:inputBiases.getSingleArray()){
			output[i] = d;
			i++;		
		}
		
		for(double d:hiddenBiases.getSingleArray()){
			output[i] = d;
			i++;		
		}
		
		return output;
		
	}
	
	public static byte[] toByteArray(double[] input){
		
		byte[] bytes = new byte[input.length*8];
		for(int i = 0; i < input.length; i++){
			ByteBuffer.wrap(bytes).putDouble(i*8, input[i]);
		}
		return bytes;
	}
	
	public static double[] toDouble(byte[] bytes){
		
		double[] output = new double[bytes.length/8];
		for(int i = 0; i < output.length; i++){
			output[i] = ByteBuffer.wrap(bytes).getDouble(i*8);
		}
		return output;
	}
	
}
