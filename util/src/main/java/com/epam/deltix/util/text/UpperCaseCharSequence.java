package com.epam.deltix.util.text;

public class UpperCaseCharSequence implements CharSequence {

    private CharSequence            delegate;

    public UpperCaseCharSequence() {
    }    
    
    public UpperCaseCharSequence(CharSequence delegate) {
        this.delegate = delegate;
    }

    public void setCharSequence(CharSequence delegate) {
        this.delegate = delegate;
    }        
    
    @Override
    public int length() {
        return delegate.length();
    }

    @Override
    public char charAt(int index) {
        final char result = delegate.charAt(index);
        return Character.isLowerCase(result) ? Character.toUpperCase(result) : result;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
}