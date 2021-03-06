#!/usr/bin/Rscript

args <- commandArgs(TRUE)

library(deisotoper)

jBenchmark(input = args[[1]], output = paste(args[[2]], "_fbdm_first.mgf", sep = ""), modus="first")
if(file.exists("/srv/lucas1/deisotoper/properties/AminoAcidMassesWithCarbamidomethylDeamidationOxidation.properties")) {
  jBenchmark(input = args[[1]], output = paste(args[[2]], "_fbdm_with_mod_first.mgf", sep = ""), modus="first", aamassfile = "/srv/lucas1/deisotoper/properties/AminoAcidMassesWithCarbamidomethylDeamidationOxidation.properties")
}

rm(list=ls())
