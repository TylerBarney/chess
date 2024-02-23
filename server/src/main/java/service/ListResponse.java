package service;

import model.GameData;

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
}
