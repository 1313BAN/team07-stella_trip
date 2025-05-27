<template>
  <Dialog :open="isOpen" @update:open="handleOpenChange">
    <DialogContent class="border-slate-700 bg-slate-900 sm:max-w-md">
      <DialogHeader>
        <DialogTitle class="text-white">여행 계획 공유</DialogTitle>
        <DialogDescription class="text-gray-400">
          아래 링크를 복사하여 다른 사람들과 여행 계획을 공유하세요.
        </DialogDescription>
      </DialogHeader>
      <div class="flex items-center space-x-2">
        <div class="grid flex-1 gap-2">
          <Label for="link" class="sr-only">링크</Label>
          <Input
            id="link"
            :value="shareLink"
            readonly
            class="border-slate-600 bg-slate-800 text-white"
          />
        </div>
        <Button type="submit" size="sm" class="px-3" @click="handleCopyLink" :disabled="isCopying">
          <span class="sr-only">복사</span>
          <Copy class="h-4 w-4" />
        </Button>
      </div>
      <DialogFooter class="sm:justify-start">
        <DialogClose asChild>
          <Button type="button" variant="secondary">닫기</Button>
        </DialogClose>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { toast } from 'vue-sonner';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogClose,
} from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { Copy } from 'lucide-vue-next';

interface Props {
  isOpen: boolean;
  shareLink: string;
}

interface Emits {
  (e: 'update:open', value: boolean): void;
  (e: 'copySuccess'): void;
  (e: 'copyError', error: Error): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

const isCopying = ref(false);

const handleOpenChange = (open: boolean) => {
  emit('update:open', open);
};

const handleCopyLink = async () => {
  if (!props.shareLink || isCopying.value) return;

  try {
    isCopying.value = true;
    await navigator.clipboard.writeText(props.shareLink);

    toast.success('링크가 클립보드에 복사되었습니다!');
    emit('copySuccess');

    // 복사 성공 후 모달 닫기
    emit('update:open', false);
  } catch (error) {
    console.error('링크 복사 실패:', error);
    toast.error('링크 복사에 실패했습니다');
    emit('copyError', error as Error);
  } finally {
    isCopying.value = false;
  }
};
</script>
