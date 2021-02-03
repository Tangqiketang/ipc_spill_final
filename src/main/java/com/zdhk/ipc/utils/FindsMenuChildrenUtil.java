package com.zdhk.ipc.utils;


import com.zdhk.ipc.dto.UserMenuDto;
import com.zdhk.ipc.entity.SysPermission;

import java.util.ArrayList;
import java.util.List;

public class FindsMenuChildrenUtil {


    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysDepart类型的list集合转换成SysDepartTreeModel类型的集合
     */
    public static List<UserMenuDto> wrapTreeDataToTreeList(List<SysPermission> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<UserMenuDto> userMenuDtoList = new ArrayList<UserMenuDto>();
        for (int i = 0; i < recordList.size(); i++) {
            SysPermission permission = recordList.get(i);
            userMenuDtoList.add(new UserMenuDto(permission));
        }
        List<UserMenuDto> tree = findChildren(userMenuDtoList);
        return tree;
    }



    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合
     */
    private static List<UserMenuDto> findChildren(List<UserMenuDto> userMenuDtoList) {

        List<UserMenuDto> treeList = new ArrayList<UserMenuDto>();
        List<UserMenuDto> treeChildrenList = new ArrayList<UserMenuDto>();
        for (int i = 0; i < userMenuDtoList.size(); i++) {
            UserMenuDto branch = userMenuDtoList.get(i);
            if (ConvertUtils.isEmpty(branch.getParentId())) {
                treeList.add(branch);
            }else{
                treeChildrenList.add(branch);
            }
        }
        getGrandChildren(treeList,treeChildrenList);

        return treeList;
    }

    /**
     * queryTreeList的子方法====3====
     *该方法是找到顶级父类下的所有子节点集合并封装到TreeList集合
     */
    private static void getGrandChildren(List<UserMenuDto> treeList,List<UserMenuDto> treeChildrenList) {
        for (int i = 0; i < treeList.size(); i++) {
            UserMenuDto parent = treeList.get(i);
            for (int j = 0; j < treeChildrenList.size(); j++){
                UserMenuDto children = treeChildrenList.get(j);
                if (children.getParentId()!=null && children.getParentId().equals(parent.getId())){
                    parent.getChildrenList().add(children);
                }
            }
            getGrandChildren(parent.getChildrenList(),treeChildrenList);
        }

    }
}
