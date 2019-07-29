package database.firebase.listeners;

import java.util.ArrayList;

public interface ReadCompleteListener<A> {
    void onReadComplete(ArrayList<A> list);
}
