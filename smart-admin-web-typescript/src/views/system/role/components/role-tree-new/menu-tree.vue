<!--
  * 角色 菜单树（新）
  * 左侧菜单树 + 右侧功能点（操作按钮）
  * a-tree 只负责展示树形结构，复选框用 a-checkbox 自渲染，状态由 checkedData 驱动
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-10-01
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <div class="role-permission-wrapper">
    <!-- 左侧：菜单树 -->
    <div class="menu-tree-panel">
      <div class="panel-header">
        <span>菜单权限</span>
      </div>
      <div class="panel-content">
        <a-tree
          :tree-data="treeData"
          :expanded-keys="expandedKeys"
          :auto-expand-parent="autoExpandParent"
          :selectable="false"
          :show-line="{ showLeafIcon: false }"
          @expand="handleExpand"
        >
          <template #title="{ dataRef }">
            <div class="menu-node">
              <a-checkbox
                class="menu-checkbox"
                :checked="isMenuChecked(dataRef.menuId)"
                @click.stop
                @change="handleMenuCheck(dataRef.menuId)"
              />
              <span class="menu-name" @click.stop="handleMenuNameClick(dataRef)">
                {{ dataRef.menuName }}
              </span>
            </div>
          </template>
        </a-tree>
      </div>
    </div>

    <!-- 右侧：功能点权限 -->
    <div class="points-panel">
      <div class="panel-header">
        <span>操作按钮权限</span>
        <span v-if="selectedMenuName" class="selected-menu-name">（当前选中：{{ selectedMenuName }}）</span>
      </div>
      <div class="panel-content">
        <template v-if="currentPoints && currentPoints.length > 0">
          <div class="points-list">
            <div v-for="point in currentPoints" :key="point.menuId" class="point-item">
              <a-checkbox
                :checked="isPointChecked(point.menuId)"
                @change="handlePointCheck(point)"
              >
                {{ point.menuName }}
              </a-checkbox>
            </div>
          </div>
        </template>
        <a-empty v-else description="请在左侧选择需要配置操作按钮的菜单" />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref, computed, watch, PropType } from 'vue';
  import { MENU_TYPE_ENUM } from '/@/constants/system/menu-const';
  import { useRoleStore } from '/@/store/modules/system/role';

  interface MenuItem {
    menuId: number;
    menuName: string;
    menuType: number;
    parentId: number;
    children?: MenuItem[];
    contextMenuId?: number;
    sort?: number;
  }

  const props = defineProps({
    tree: {
      type: Array as PropType<MenuItem[]>,
      default: () => [],
    },
  });

  const roleStore = useRoleStore();

  const expandedKeys = ref<number[]>([]);
  const autoExpandParent = ref(true);
  const selectedMenuId = ref<number | null>(null);

  // 当前选中菜单的功能点
  const currentPoints = computed(() => {
    if (!selectedMenuId.value) return [];
    const menu = findMenuById(props.tree, selectedMenuId.value);
    if (!menu) return [];
    const points = menu.children?.filter((child) => child.menuType === MENU_TYPE_ENUM.POINTS.value) || [];
    return points.sort((a, b) => (a.sort || 0) - (b.sort || 0));
  });

  // 当前选中菜单名称
  const selectedMenuName = computed(() => {
    if (!selectedMenuId.value) return '';
    const menu = findMenuById(props.tree, selectedMenuId.value);
    return menu?.menuName || '';
  });

  // 转换菜单树为 a-tree 展示格式（过滤掉功能点）
  const treeData = computed(() => {
    return props.tree.map((item) => convertToTreeNode(item));
  });

  function convertToTreeNode(menu: MenuItem): any {
    const children = menu.children
      ?.filter((child) => child.menuType !== MENU_TYPE_ENUM.POINTS.value)
      ?.map((child) => convertToTreeNode(child));

    return {
      key: menu.menuId,
      menuId: menu.menuId,
      menuName: menu.menuName,
      children: children?.length ? children : undefined,
    };
  }

  function findMenuById(tree: MenuItem[], id: number): MenuItem | null {
    for (const menu of tree) {
      if (menu.menuId === id) return menu;
      if (menu.children) {
        const found = findMenuById(menu.children, id);
        if (found) return found;
      }
    }
    return null;
  }

  // 菜单复选框状态：直接读 checkedData
  function isMenuChecked(menuId: number): boolean {
    return roleStore.checkedData.includes(menuId);
  }

  // 功能点复选框状态：直接读 checkedData
  function isPointChecked(menuId: number): boolean {
    return roleStore.checkedData.includes(menuId);
  }

  // 点击菜单名：只展开/收起下级 + 展示按钮权限框，不改变选中状态
  function handleMenuNameClick(dataRef: any) {
    selectedMenuId.value = dataRef.menuId;
    const key = dataRef.menuId;
    const idx = expandedKeys.value.indexOf(key);
    if (idx === -1) {
      expandedKeys.value = [...expandedKeys.value, key];
    } else {
      expandedKeys.value = expandedKeys.value.filter((k) => k !== key);
    }
    autoExpandParent.value = false;
  }

  // 菜单复选框点击：对当前状态取反
  function handleMenuCheck(menuId: number) {
    const checked = !isMenuChecked(menuId);
    selectedMenuId.value = menuId;
    const fullMenu = findMenuById(props.tree, menuId);
    if (!fullMenu) return;

    if (checked) {
      // 选中菜单：递归选中该菜单及所有子菜单、功能点
      roleStore.addCheckedDataAndChildren(fullMenu);
      // 级联选中所有上级父菜单
      roleStore.selectUpperLevel(fullMenu);
    } else {
      // 取消菜单：递归取消该菜单及所有子菜单、功能点
      roleStore.deleteCheckedDataAndChildren(fullMenu);
    }
  }

  // 功能点复选框点击：对当前状态取反，只影响单个按钮
  function handlePointCheck(point: MenuItem) {
    const checked = !isPointChecked(point.menuId);
    if (checked) {
      // 选中功能点：同时选中父菜单
      roleStore.addCheckedData(point.menuId);
      roleStore.addCheckedData(point.parentId);
    } else {
      // 取消功能点：只取消该功能点，不影响父菜单和其他功能点
      roleStore.removeCheckedData(point.menuId);
    }
  }

  function handleExpand(keys: number[]) {
    expandedKeys.value = keys;
    autoExpandParent.value = false;
  }

  function initExpandedKeys(tree: MenuItem[]): number[] {
    const keys: number[] = [];
    function collectKeys(nodes: MenuItem[]) {
      nodes.forEach((node) => {
        if (node.children && node.children.length > 0) {
          keys.push(node.menuId);
          collectKeys(node.children);
        }
      });
    }
    collectKeys(tree);
    return keys;
  }

  // tree 变化时，初始化展开keys
  watch(
    () => props.tree,
    (newTree) => {
      if (newTree && newTree.length > 0) {
        expandedKeys.value = initExpandedKeys(newTree);
      }
    },
    { immediate: true }
  );
</script>
<style scoped lang="less">
  .role-permission-wrapper {
    display: flex;
    gap: 24px;
    height: 100%;
    min-height: 400px;

    .menu-tree-panel,
    .points-panel {
      flex: 1;
      display: flex;
      flex-direction: column;
      border: 1px solid #f0f0f0;
      border-radius: 4px;
      overflow: hidden;

      .panel-header {
        padding: 12px 16px;
        background: #fafafa;
        border-bottom: 1px solid #f0f0f0;
        font-weight: 500;
        color: #333;

        .selected-menu-name {
          font-weight: normal;
          color: #666;
          font-size: 12px;
        }
      }

      .panel-content {
        flex: 1;
        overflow: auto;
        padding: 12px;
      }
    }
  }

  .menu-node {
    display: flex;
    align-items: center;
    gap: 8px;

    .menu-name {
      cursor: pointer;
      user-select: none;
    }
  }

  .menu-checkbox {
    :deep(.ant-checkbox + span) {
      color: inherit;
    }
  }

  .points-list {
    display: flex;
    flex-direction: column;
    gap: 12px;

    .point-item {
      :deep(.ant-checkbox-wrapper) {
        margin-left: 0;
      }
    }
  }
</style>
