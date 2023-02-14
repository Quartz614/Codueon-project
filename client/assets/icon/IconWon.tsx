interface Props {
  width?: string;
  heigth?: string;
}

export const IconWon = ({ width, heigth }: Props) => {
  return (
    <svg
      width={width}
      height={heigth}
      viewBox="0 0 20 19"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M2.43739 0.930883C2.21867 0.218499 1.50781 -0.163136 0.855538 0.0700851C0.203267 0.303306 -0.152163 1.0793 0.0626573 1.78744L2.01556 8.14377H1.25003C0.558696 8.14377 0.000164222 8.75015 0.000164222 9.50069C0.000164222 10.2512 0.558696 10.8576 1.25003 10.8576H2.8475L5.0621 18.0705C5.23786 18.643 5.74171 19.0204 6.29634 18.9992C6.85096 18.9779 7.32747 18.5581 7.46027 17.973L9.10071 10.8576H10.8974L12.5378 17.973C12.6706 18.5581 13.1471 18.9779 13.7018 18.9992C14.2564 19.0204 14.7602 18.643 14.936 18.0705L17.1506 10.8576H18.7481C19.4394 10.8576 19.9979 10.2512 19.9979 9.50069C19.9979 8.75015 19.4394 8.14377 18.7481 8.14377H17.9825L19.9354 1.78744C20.1542 1.07506 19.7987 0.307546 19.1465 0.0700851C18.4942 -0.167376 17.7833 0.218498 17.5646 0.926642L15.3461 8.14377H12.8503L11.2099 1.02841C11.0732 0.426277 10.5732 0.00223902 9.99905 0.00223902C9.4249 0.00223902 8.92495 0.426277 8.78825 1.02841L7.14781 8.14377H4.64808L2.43739 0.930883ZM5.48393 10.8576H6.52288L6.07761 12.7912L5.48393 10.8576ZM9.72564 8.14377L9.99905 6.95222L10.2725 8.14377H9.72564ZM13.4752 10.8576H14.5142L13.9205 12.7912L13.4752 10.8576Z"
        fill="black"
        fillOpacity="0.58"
      />
    </svg>
  );
};