package jjapra.app.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class Pairs<T, T1> {
    public T key;
    public T1 value;

    public Pairs(T id, T1 string) {
        this.key = id;
        this.value = string;
    }
}
