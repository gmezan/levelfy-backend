package com.uc.backend.repository;

import com.uc.backend.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository  extends JpaRepository<Video, Integer> {

    /*
    public List<Video> findVideosByClase_Idclase(int i);

    public List<Video> findVideosByClase_IdclaseAndEvaluacionOrderByOrdenAsc(int i, int j);



     */
}
