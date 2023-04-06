<script setup lang="ts">
import {ref} from "vue";
import axios from 'axios'
//const count = ref(0)

const title = ref("")
const content = ref("")

const write =  function (){
    console.log(title.value,content.value)
    axios.post('/api/posts',{
        title:title.value,
        content:content.value
    })
}
</script>

<template>
<!--<p>안녕하세요</p> <button @click="count += 1">버튼을 눌러주세요.</button>
  <p>{{count}}</p>-->
  <div class="mt-2">
      <el-input v-model="title" type="text" placeholder="제목을 입력해주세요"/>
  </div>
  <div class="mt-2">
      <el-input v-model="content" type="textarea" rows="15"></el-input>
  </div>

  <el-button type="primary" @click="write()">글 작성완료</el-button>
</template>

<style scoped>

</style>