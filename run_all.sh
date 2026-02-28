#!/bin/bash
# Run all Java Enum lessons in order
cd "$(dirname "$0")/src"

echo "======================================"
echo "  Java Enums - Complete Course Runner"
echo "======================================"

files=(
  "Lesson1_BasicEnum"
  "Lesson2_BuiltInMethods"
  "Lesson3_FieldsMethodsConstructors"
  "Lesson4_AbstractMethods"
  "Lesson5_Interfaces"
  "Lesson6_EnumSet"
  "Lesson7_EnumMap"
  "Lesson8_Singleton"
  "Lesson9_StrategyPattern"
  "Lesson10_StateMachine"
  "CoffeeShopSystem"
)

for f in "${files[@]}"; do
  echo ""
  echo "======================================"
  echo "  Running: $f"
  echo "======================================"
  javac "$f.java" && java "$f"
  if [ $? -ne 0 ]; then
    echo "ERROR: $f failed to compile or run"
    exit 1
  fi
done

echo ""
echo "======================================"
echo "  All lessons completed successfully!"
echo "======================================"
