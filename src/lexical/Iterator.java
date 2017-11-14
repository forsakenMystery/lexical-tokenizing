/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical;

/**
 *
 * @author FORSAKEN MYSTERY
 */
public class Iterator {
    private int pointer;
    private static Iterator iterator=new Iterator();
    private Iterator(){
        pointer=0;
    }
    public boolean hasNext(int length){
        return pointer<length;
    }
    
    public static Iterator get(){
        return iterator;
    }
    public int next(){
        return pointer++;
    }
    public void retrack(){
        pointer--;
    }
    public void reset(){
        pointer=0;
    }
    
}
