import axios from "axios";

const getBasicInfo = async (lessonId: number) => {
  return await axios.get(`/lesson/${lessonId}`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
      Authorization: `${
        localStorage.getItem("token")
          ? `Bearer ${localStorage.getItem("token")}`
          : null
      }`,
    },
  });
};

export default getBasicInfo;