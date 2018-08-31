package in.abhishek.LibraryModule.Data;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

abstract class Information {

    String _ID;
    String name;

    public Information(String name, String id){
        this.name = name;
        _ID = id;
    }

    public Information(){

    }

    protected void setID(String id){
        _ID = id;
    }

    protected void setName(String name){
        this.name = name;
    }

    public String getId(){
        return _ID;
    }

    protected String getName(){
        return name;
    }

    abstract public String details();
}