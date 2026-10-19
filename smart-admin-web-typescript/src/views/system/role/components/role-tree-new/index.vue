<!--
  * 角色 树形结构（新）
  * 左侧菜单树 + 右侧功能点（操作按钮）
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-10-01
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <div class="role-tree-new">
    <div class="tree-header">
      <p>设置角色对应的功能操作、后台管理权限</p>
      <a-button v-if="selectRoleId" type="primary" @click="saveChange" v-privilege="'system:role:menu:update'">
        保存
      </a-button>
    </div>
    <!-- 功能权限勾选部分 -->
    <div class="tree-content">
      <MenuTree v-if="tree && tree.length > 0" :tree="tree" :default-expand-all="true" />
      <a-empty v-else-if="!selectRoleId" description="请先选择角色" />
      <a-empty v-else description="暂无菜单数据" />
    </div>
  </div>
</template>
<script setup lang="ts">
  import { inject, ref, watch } from 'vue';
  import { message } from 'ant-design-vue';
  import _ from 'lodash';
  import MenuTree from './menu-tree.vue';
  import { roleMenuApi } from '/@/api/system/role-menu-api';
  import { useRoleStore } from '/@/store/modules/system/role';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const roleStore = useRoleStore();
  const tree = ref<any[]>([]);
  const selectRoleId = inject<{ value: number }>('selectRoleId');

  watch(selectRoleId, () => getRoleSelectedMenu(), {
    immediate: true,
  });

  async function getRoleSelectedMenu() {
    if (!selectRoleId?.value) {
      return;
    }
    const res = await roleMenuApi.getRoleSelectedMenu(selectRoleId.value);
    const data = res.data;
    if (_.isEmpty(roleStore.treeMap)) {
      roleStore.initTreeMap(data.menuTreeList || []);
    }
    roleStore.initCheckedData(data.selectedMenuId || []);
    tree.value = data.menuTreeList || [];
  }

  async function saveChange() {
    const checkedData = roleStore.checkedData;
    if (_.isEmpty(checkedData)) {
      message.error('还未选择任何权限');
      return;
    }
    const params = {
      roleId: selectRoleId?.value,
      menuIdList: checkedData,
    };
    SmartLoading.show();
    try {
      await roleMenuApi.updateRoleMenu(params);
      message.success('保存成功');
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }
</script>
<style scoped lang="less">
  @import 'index.less';
</style>
