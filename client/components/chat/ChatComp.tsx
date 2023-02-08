import BackIcon from "assets/icon/BackIcon";
import { useRecoilState } from "recoil";
import { useState, useEffect } from "react";
import { chatActive } from "atoms/chat/chatAtom";
import ChatContent from "./ChatContent";
import { chatListState, roomIdState, newDataState } from "atoms/chat/chatAtom";
import { sendMsg, subscribeRoom, unSubscribeRoom } from "hooks/chat/socket";

const ChatComp = () => {
  const [active, setActive] = useRecoilState(chatActive);
  const [inputValue, setInputValue] = useState<string>("");
  const [chatList, setChatList] = useRecoilState(chatListState);
  const [roomId, setRoomId] = useRecoilState(roomIdState);

  const handleClickCancel = () => {
    setActive(false);
    unSubscribeRoom();
  };

  const handleInputChange = (e: any) => {
    setInputValue(e.target.value);
  };

  const handlePressEnter = (e: any) => {
    if (e.key === "Enter" && inputValue) {
      sendMsg(roomId, inputValue);
      setInputValue("");
    }
  };

  const handleClickSubmit = (e: any) => {
    if (inputValue) {
      sendMsg(roomId, inputValue);
      setInputValue("");
    }
  };

  return (
    <>
      <div className="tablet:w-2/3 w-full h-full overflow-auto flex flex-col justify-start items-center">
        {/* header */}
        <div className="w-[95%] h-[50px] flex flex-row justify-center items-center border-b border-borderColor/30">
          <div className="w-fit h-fit font-SCDream4 text-textColor tablet:text-md text-sm">
            과외쌤이름
          </div>
          <div
            className="absolute right-6 w-fit h-fit cursor-pointer"
            onClick={handleClickCancel}
          >
            <BackIcon />
          </div>
        </div>
        {/* content */}
        <ChatContent chatList={chatList} />
        {/* input */}
        <div className="flex flex-row justify-center items-center w-[95%] h-[10%] border-t border-borderColor/30 ">
          <input
            type="text"
            value={inputValue}
            onChange={handleInputChange}
            onKeyDown={handlePressEnter}
            placeholder="메세지를 입력하세요"
            className="w-full h-full rounded-xl outline-none bg-bgColor font-SCDream3 text-textColor text-sm p-3"
          />
          <button
            className="w-16 h-fit p-2 bg-pointColor rounded-md font-SCDream3 text-xs text-white flex flex-col justify-center items-center"
            onClick={handleClickSubmit}
          >
            전송
          </button>
        </div>
      </div>
    </>
  );
};

export default ChatComp;
