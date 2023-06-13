package ru.yandex.practicum.filmorate.storage.implementations;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

public class MpaStorageImpl implements MpaStorage {
    @Override
    public Optional<Mpa> getMpaById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Mpa> getAllMpa() {
        return null;
    }
}
