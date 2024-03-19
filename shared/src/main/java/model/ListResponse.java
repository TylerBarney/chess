package model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ListResponse {
    public ListEntry[] games;

    public ListResponse(Collection<GameData> gameList){
        games = new ListEntry[gameList.size()];
        Iterator iter = gameList.iterator();
        int i = 0;
        while(iter.hasNext()){
            games[i] = new ListEntry((GameData) iter.next());
            i++;
        }
    }
    public ListResponse(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListResponse that = (ListResponse) o;
        return Arrays.equals(games, that.games);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(games);
    }

    @Override
    public String toString() {
        String out = "";
        out += "Games: \n";
        for (int i = 0; i <games.length; i++){
            out += i + ": " + games[i].toString() + "\n";
        }
        if (games.length == 0) out += "no games created";
        return out;
    }
}
