package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDao;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {

    private final MpaDao mpaDao;

    public Mpa getMpaById(Integer id) {
        log.info("Поиск Mpa по Id {}", id);
        Optional<Mpa> mpa = mpaDao.getMpaById(id);
        if (mpa.isEmpty())
            throw new NotFoundException("Mpa не найден по Id " + id);
        log.info("Найден Mpa по Id {}", id);
        return mpa.get();
    }

    public List<Mpa> getAllMpa() {
        log.info("Вывод всех Mpa");
        return mpaDao.getAllMpa();
    }
}
