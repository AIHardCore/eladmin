<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">留言id</label>
        <el-input v-model="query.commentId" clearable placeholder="留言id" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">发表用户的openId</label>
        <el-input v-model="query.to" clearable placeholder="发表用户的openId" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">点赞用户的openId</label>
        <el-input v-model="query.from" clearable placeholder="点赞用户的openId" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="留言id" prop="commentId">
            <el-input v-model="form.commentId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="发表用户的openId" prop="to">
            <el-input v-model="form.to" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="点赞用户的openId" prop="from">
            <el-input v-model="form.from" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="key.commentId" label="留言id" />
        <el-table-column prop="key.to" label="发表用户的openId" />
        <el-table-column prop="key.from" label="点赞用户的openId" />
        <el-table-column prop="createTime" label="创建日期" />
        <el-table-column v-if="checkPer(['admin','commentLike:edit','commentLike:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudCommentLike from '@/api/commentLike'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { id: null, commentId: null, to: null, from: null, createBy: null, updateBy: null, createTime: null, updateTime: null }
export default {
  name: 'CommentLike',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: '点赞记录', url: 'api/commentLike', idField: 'id', sort: 'id,desc', crudMethod: { ...crudCommentLike }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'commentLike:add'],
        edit: ['admin', 'commentLike:edit'],
        del: ['admin', 'commentLike:del']
      },
      rules: {
        commentId: [
          { required: true, message: '留言id不能为空', trigger: 'blur' }
        ],
        to: [
          { required: true, message: '发表用户的openId不能为空', trigger: 'blur' }
        ],
        from: [
          { required: true, message: '点赞用户的openId不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'commentId', display_name: '留言id' },
        { key: 'to', display_name: '发表用户的openId' },
        { key: 'from', display_name: '点赞用户的openId' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
