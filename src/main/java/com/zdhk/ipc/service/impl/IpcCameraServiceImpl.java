package com.zdhk.ipc.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdhk.ipc.entity.IpcCamera;
import com.zdhk.ipc.mapper.IpcCameraMapper;
import com.zdhk.ipc.service.IIpcCameraService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wang Min
 * @since 2020-11-13
 */
@Service
public class IpcCameraServiceImpl extends ServiceImpl<IpcCameraMapper, IpcCamera> implements IIpcCameraService {

}
