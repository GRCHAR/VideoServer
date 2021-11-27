package com.forum.video.service;

import com.forum.video.bo.Live;

/**
 * @author genghaoran
 */
public interface ILiveService {
    Live createLive(int userId, String title);

    Live getLive(int userId);

    Live updateLiveTitle(String title, int userId);

    Live startLive(int userId);
}
