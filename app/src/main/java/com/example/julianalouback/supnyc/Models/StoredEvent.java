package com.example.julianalouback.supnyc.Models;

/**
 * Created by moldy530 on 12/20/14.
 */
public class StoredEvent {
    private String mKey;
    private String mType;

    public StoredEvent(String key, String type){
        mKey = key;
        mType = type;
    }

    public String getKey(){
        return mKey;
    }
    public void setName(String name){
        this.mKey = name;
    }

    public String getType() {
        return mType;
    }
    public void setType(String type){
        this.mType = type;
    }

    @Override
    public boolean equals(Object obj){

        if(obj instanceof Event){
            Event e = (Event) obj;
            return e.getRangeKey().equals(this.getKey()) && e.getType().equals(this.getType());
        }
        else if (obj instanceof StoredEvent){
            StoredEvent e = (StoredEvent) obj;
            return e.getKey().equals(this.getKey()) && e.getType().equals(this.getType());
        }

        return false;
    }

}
