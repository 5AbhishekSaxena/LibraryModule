package in.abhishek.LibraryModule.Data;

abstract class Information {

    String _ID;
    String name;

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
