/** @type {import('next').NextConfig} */
const removeImports = require("next-remove-imports")();

const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
//   images:{
//     loader: 'imgix',
//     path: '/',
//   },
  images: {
    loader: "akamai",
    path: "",
  },
};

module.exports = removeImports({
  ...nextConfig,
});