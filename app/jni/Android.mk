
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := romide 
LOCAL_SRC_FILES := romide.c

include $(BUILD_SHARED_LIBRARY)

