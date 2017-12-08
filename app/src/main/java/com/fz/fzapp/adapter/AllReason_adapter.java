package com.fz.fzapp.adapter;

import com.fz.fzapp.model.ReasonResponse;

import java.util.List;

/**
 * Created by Heru Permana on 10/16/2017.
 */

public class AllReason_adapter {
    private List<ReasonResponse> allresponsefail;
    private List<ReasonResponse> allresponselate;

    public List<ReasonResponse> getAllresponsefail()
    {
        return allresponsefail;
    }

    public void setAllresponsefail(List<ReasonResponse> allresponsefail)
    {
        this.allresponsefail = allresponsefail;
    }

    public List<ReasonResponse> getAllresponselate()
    {
        return allresponselate;
    }

    public void setAllresponselate(List<ReasonResponse> allresponselate)
    {
        this.allresponselate = allresponselate;
    }

    private static AllReason_adapter UserInstance = new AllReason_adapter();

    public static AllReason_adapter getInstance()
    {
        return UserInstance;
    }


    private AllReason_adapter()
    {
    }

    public static void initAllTaskList()
    {
        UserInstance = new AllReason_adapter();
    }
}
