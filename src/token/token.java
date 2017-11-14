/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package token;

import jdk.nashorn.internal.parser.TokenType;

/**
 *
 * @author FORSAKEN MYSTERY
 */
public class token {
    private String statement;
    private tokenType tokenType;
    private int row;
    private int col;
    private String tokenTypeName;
    
    public token(String statement, tokenType tokenType, int row, int col, String tokenTypeName) {
        this.statement = statement;
        this.tokenType = tokenType;
        this.row = row;
        this.col = col;
        this.tokenTypeName = tokenTypeName;
    }

    @Override
    public String toString() {
        return statement+" "+tokenType+" "+tokenTypeName+"\n"; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
