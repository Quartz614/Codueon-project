import detailDelete from "apis/detail/deleteDetail";
import { useMutation } from "react-query";

const useDetailDelete = () => {
  return useMutation(detailDelete, {
    onSuccess: res => {
      console.log("success");
    },
    onError: err => {
      console.log("success");
    },
  });
};
export default useDetailDelete;
